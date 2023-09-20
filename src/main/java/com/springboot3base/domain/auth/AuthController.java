package com.springboot3base.domain.auth;

import com.springboot3base.common.exception.AuthAccessDeniedException;
import com.springboot3base.common.exception.AuthEntryPointException;
import com.springboot3base.common.exception.AuthTokenExpiredException;
import com.springboot3base.common.model.response.BaseResponse;
import com.springboot3base.common.model.response.CommonBaseResult;
import com.springboot3base.common.model.response.CommonIdResult;
import com.springboot3base.common.model.response.CommonResult;
import com.springboot3base.common.security.JwtTokenProvider;
import com.springboot3base.domain.auth.dto.AuthChangePassDto;
import com.springboot3base.domain.auth.dto.SignInReqDto;
import com.springboot3base.domain.auth.dto.SignInResDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "01. Auth")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1")
public class AuthController {
    private final AuthService authService;
    private final BaseResponse baseResponse;

    private final JwtTokenProvider jwtTokenProvider;

    @Operation(hidden = true)
    @RequestMapping(value = "/exception/entrypoint")
    public CommonBaseResult entryPointException(){
        throw new AuthEntryPointException();
    }

    @Operation(hidden = true)
    @RequestMapping(value = "/exception/accessdenied")
    public CommonBaseResult accessDeniedException(){
        throw new AuthAccessDeniedException();
    }

    @Operation(hidden = true)
    @RequestMapping(value = "/exception/tokenexpired")
    public CommonBaseResult tokenExpired(){
        throw new AuthTokenExpiredException();
    }

    @Operation(summary = "login", description = "login")
    @PostMapping(value = "/auth/sign-in")
    public CommonResult<SignInResDto> signIn(@Parameter(required = true, name = "reqDto", description = "Login user info") @RequestBody @Valid SignInReqDto reqDto) {
        return baseResponse.getContentResult(authService.signIn(reqDto));
    }

    @Operation(summary = "logout", description = "logout")
    @PostMapping(value = "/auth/log-out")
    public CommonBaseResult postLogOut(@RequestHeader(value = "x-api-token") String accessToken) {
        authService.logout(Long.parseLong(jwtTokenProvider.getUserIdFromToken(accessToken)));
        return baseResponse.getSuccessResult();
    }

    @Operation(summary = "refresh token")
    @PostMapping(value = "/auth/refresh-token/{id}")
    public CommonResult<SignInResDto> refresh(@RequestHeader(value = "x-api-token") String accessToken,
                                              @RequestHeader(value = "x-refresh-token") String refreshToken) {
        return baseResponse.getContentResult(authService.refreshToken(accessToken, refreshToken));
    }

    @Operation(summary = "reset password")
    @PostMapping(value = "/auth/reset-pw")
    public CommonResult<CommonIdResult> resetPw(@Parameter(required = true, description = "param code") @RequestBody AuthChangePassDto dto) throws Exception{
        return baseResponse.getContentResult(authService.resetPw(dto));
    }
}
