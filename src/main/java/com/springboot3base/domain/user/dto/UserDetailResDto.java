package com.springboot3base.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "Detail user response")
@Data
@AllArgsConstructor
public class UserDetailResDto {
    @Schema(example = "1")
    private Long id;

    @Schema(example = "test01")
    private String username;

    @Schema(example = "이름")
    private String name;

    @Schema(example = "ROLE_ADMIN")
    private String roleName;

    @Schema(example = "010-1234-1234")
    private String phone;

    @Schema(example = "example@seobuk.kr")
    private String email;

    @Schema(example = "2023-08-31T00:00:00")
    private LocalDateTime createDate;
}
