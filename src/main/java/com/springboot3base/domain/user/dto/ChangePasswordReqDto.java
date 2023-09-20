package com.springboot3base.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Schema(description = "Changing passwd info")
@Data
public class ChangePasswordReqDto {
    @Schema(description = "original passwd", example = "password")
    @NotBlank
    private String oldPassword;

    @Schema(description = "new passwd", example = "password")
    @NotBlank
    private String newPassword;
}
