package com.springboot3base.domain.user;

import com.springboot3base.common.model.response.*;
import com.springboot3base.common.security.JwtTokenProvider;
import com.springboot3base.domain.user.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Tag(name = "02. User")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/v1/user")
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;
    private final BaseResponse baseResponse;

    @Operation(summary = "Signup new account")
    @PostMapping(value = "/sign-up")
    public CommonResult<CommonIdResult> addUser(@Parameter(required = true, name = "reqDto", description = "Signup user info") @RequestBody @Valid UserReqDto reqDto) {
        return baseResponse.getContentResult(userService.addUser(reqDto));
    }

    @Operation(summary = "Approve new account")
    @PatchMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonResult<CommonIdResult> approval(@Parameter(required = true, name = "id", description = "User's id") @PathVariable Long id) {
        return baseResponse.getContentResult(userService.approval(id));
    }

    @Operation(summary = "Update user")
    @PutMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonResult<CommonIdResult> updateUser(@Parameter(required = true, name = "id", description = "User's id") @PathVariable Long id,
                                                   @Parameter(required = true, name = "reqDto", description = "modifying user info") @RequestBody @Valid UserUpdateReqDto reqDto) {
        return baseResponse.getContentResult(userService.updateUser(id, reqDto));
    }

    @Operation(summary = "Delete user")
    @DeleteMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonResult<CommonIdResult> deleteUser(@Parameter(required = true, name = "id", description = "User's id") @PathVariable Long id) {
        return baseResponse.getContentResult(userService.deleteUser(id));
    }

    @Operation(summary = "Get detail user")
    @GetMapping(value = "/{id}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonResult<UserDetailResDto> getDetail(@Parameter(required = true, name = "id", description = "User's id") @PathVariable Long id) {
        return baseResponse.getContentResult(userService.getDetail(id));
    }

    @Operation(summary = "forgot password")
    @PostMapping(value = "/find-pw")
    public CommonBaseResult forgotPasswd(@Parameter(required = true, name = "username", description = "username") @RequestParam String userId,
                                         @Parameter(required = true, name = "name", description = "name") @RequestParam String name,
                                         @Parameter(required = true, name = "email", description = "email") @RequestParam String email) throws Exception {
        userService.forgotPasswd(userId, name, email);
        return baseResponse.getSuccessResult();
    }

    @Operation(summary = "admin change user's passwd")
    @PatchMapping(value = "/change-my-password")
    public CommonResult<CommonIdResult> changePassword(@RequestHeader(value = "x-api-token") String accessToken,
                                                       @Parameter(required = true, name = "reqDto", description = "비밀번호 변경 요청 정보") @RequestBody @Valid ChangePasswordReqDto reqDto) {
        Long loggedUserId = Long.parseLong(jwtTokenProvider.getUserIdFromToken(accessToken));
        return baseResponse.getContentResult(userService.changePassword(loggedUserId, reqDto));
    }

    @Deprecated
    @Operation(summary = "reset password")
    @PatchMapping(value = "/resetPassword/{id}")
    public CommonResult<CommonIdResult> resetPassword(@Parameter(required = true, name = "id", description = "아이디") @PathVariable Long id) {
        return baseResponse.getContentResult(userService.resetPassword(id));
    }

    @Operation(summary = "filter")
    @PostMapping(value = "/filter")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public CommonResult<PageContentResDto<UserResDto>> getStaffUserList(@Parameter(required = true, name = "reqDto", description = "Filtering user parameters") @RequestBody UserFilterReqDto reqDto,
                                                            @PageableDefault(sort="createDate", direction = Sort.Direction.DESC) @ParameterObject Pageable pageable) {
        return baseResponse.getContentResult(userService.filter(reqDto, pageable));
    }

}
