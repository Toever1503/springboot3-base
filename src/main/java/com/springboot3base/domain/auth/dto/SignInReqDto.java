package com.springboot3base.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "Login user info")
@Data
@NoArgsConstructor
public class SignInReqDto {
    @Schema(description = "username", example = "admin")
    @NotBlank
    private String username;

    @Schema(description = "passwd", example = "123456")
    @NotBlank
    private String password;
}
