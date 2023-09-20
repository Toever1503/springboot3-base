package com.springboot3base.domain.auth;

import com.google.gson.Gson;
import com.springboot3base.common.exception.*;
import com.springboot3base.common.model.response.CommonIdResult;
import com.springboot3base.common.security.EncryptProvider;
import com.springboot3base.common.security.JwtTokenProvider;
import com.springboot3base.domain.auth.dto.AuthChangePassDto;
import com.springboot3base.domain.auth.dto.AuthPasswordCodeDto;
import com.springboot3base.domain.auth.dto.SignInReqDto;
import com.springboot3base.domain.auth.dto.SignInResDto;
import com.springboot3base.domain.auth.entity.AuthenticationEntity;
import com.springboot3base.domain.auth.repository.AuthenticationRepository;
import com.springboot3base.domain.user.entity.UserEntity;
import com.springboot3base.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationRepository authenticationRepository;
    private final EncryptProvider encryptProvider;


    @Transactional
    public SignInResDto signIn(SignInReqDto reqDto) {
        UserEntity userEntity = userRepository.findByUsernameAndDel(reqDto.getUsername(), false).orElseThrow(() -> new SigninFailedException("ID/PW"));

        if (userEntity.getApproved().equals(false))
            throw new SigninFailedException("Not approved.");

        if (userEntity.getPassword() != null) {
            // check password
            if (!passwordEncoder.matches(reqDto.getPassword(), userEntity.getPassword()))
                throw new SigninFailedException("ID/PW");
            return generateToken(userEntity);
        } else {
            // check tmpPassword
            if (!passwordEncoder.matches(reqDto.getPassword(), userEntity.getTmpPassword()))
                throw new SigninFailedException("ID/PW");

            return SignInResDto.builder()
                    .userId(userEntity.getUsername())
                    .roles(List.of(userEntity.getRole().getRoleName()))
                    .setPassword(false)
                    .build();
        }
    }

    @Transactional
    public void logout(Long id) {
        AuthenticationEntity authenticationEntity = authenticationRepository.findFirstByUserIdAndHasRevokedOrderByCreateDateDesc(id, false).orElseThrow(ObjectNotFoundException::new);
        authenticationEntity.setLogout();
        authenticationRepository.saveAndFlush(authenticationEntity);
    }

    private SignInResDto generateToken(UserEntity userEntity) {
        StringBuilder expirationTime = new StringBuilder();
        String newAccessToken = jwtTokenProvider.createToken(String.valueOf(userEntity.getId()), List.of(userEntity.getRole().getRoleName()), false, null, expirationTime);
        StringBuilder refreshExpirationTime = new StringBuilder();
        String newRefreshToken = jwtTokenProvider.createToken(String.valueOf(userEntity.getId()), List.of(userEntity.getRole().getRoleName()), true, null, refreshExpirationTime);

        // save authentication information
        authenticationRepository.save(AuthenticationEntity.builder()
                .id(UUID.randomUUID().toString())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .userId(userEntity.getId())
                .hasRevoked(false)
                .build());

        return SignInResDto.builder()
                .userId(userEntity.getUsername())
                .accessToken(newAccessToken)
                .refreshToken(newRefreshToken)
                .accessExpireIn(expirationTime.toString())
                .refreshExpireIn(refreshExpirationTime.toString())
                .roles(List.of(userEntity.getRole().getRoleName()))
                .build();
    }

    @Transactional
    public SignInResDto refreshToken(String accessToken, String refreshToken) {
        if (jwtTokenProvider.validateToken(refreshToken)) {
            Long id = Long.valueOf(jwtTokenProvider.getUserIdFromToken(refreshToken));
            AuthenticationEntity authenticationEntity = authenticationRepository.findByRefreshToken(refreshToken).orElseThrow(IllegalArgumentException::new);
            UserEntity userEntity = userRepository.findById(authenticationEntity.getUserId()).orElseThrow(UserNotFoundException::new);
            if (userEntity.getId().equals(id) && authenticationEntity.getAccessToken().equals(accessToken)) {
                return generateToken(userEntity);
            }
        }
        throw new IllegalArgumentException();
    }

    @Transactional
    public CommonIdResult resetPw(AuthChangePassDto dto) {
        // code check (id, time), base64 decoding and decript
        // {"id":1,"expire":"2021060101"}

        String decCode;
        try {
            decCode = encryptProvider.decAES(dto.getCode());
        } catch (Exception e) {
            throw new InvalidRequstException();
        }

        Gson gson = new Gson();
        AuthPasswordCodeDto codedto = gson.fromJson(decCode, AuthPasswordCodeDto.class);

        // check expire time
        LocalDateTime dateTime = LocalDateTime.parse(codedto.getExpire(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        if (LocalDateTime.now().isAfter(dateTime)) {
            throw new AuthExpireException();
        }

        // check user
        UserEntity userEntity = userRepository.findById(codedto.getId()).orElseThrow(UserNotFoundException::new);
        // encryption password and save password
        userEntity.setPassword(passwordEncoder.encode(dto.getNewPassword()));
        return new CommonIdResult(userEntity.getId());
    }
}
