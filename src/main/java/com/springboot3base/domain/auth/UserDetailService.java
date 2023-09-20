package com.springboot3base.domain.auth;

import com.springboot3base.common.exception.UserNotFoundException;
import com.springboot3base.domain.auth.entity.RoleEntity;
import com.springboot3base.domain.user.entity.UserEntity;
import com.springboot3base.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RequiredArgsConstructor
@Service
public class UserDetailService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findById(Long.valueOf(username)).orElseThrow(()->new UserNotFoundException("user"));
        userEntity.setAuthorities(List.of(new SimpleGrantedAuthority(userEntity.getRole().getRoleName())));
        return userEntity;
    }
}
