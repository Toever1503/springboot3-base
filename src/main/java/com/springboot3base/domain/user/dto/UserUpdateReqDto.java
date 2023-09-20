package com.springboot3base.domain.user.dto;

import com.springboot3base.common.enums.RoleEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Modifying user info")
@Data
@NoArgsConstructor
public class UserUpdateReqDto {
    @Schema(description = "ROLE", example = "ROLE_USER")
    private RoleEnum role;

    @Schema(description = "User's name", example = "이름")
    @NotBlank
    private String name;
    
    @Schema(description = "User's phone", example = "010-1234-1234")
    @NotBlank
    private String phone;

    @Schema(description = "User's email", example = "example@seobuk.kr")
    private String email;
}
