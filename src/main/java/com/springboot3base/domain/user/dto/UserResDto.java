package com.springboot3base.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Schema(description = "User info response")
@Data
@AllArgsConstructor
public class UserResDto {
    @Schema(description = "아이디", example = "1")
    private Long id;

    @Schema(description = "사용자 아이디", example = "test01")
    private String username;

    @Schema(description = "이름", example = "이름")
    private String name;

    @Schema(description = "ROLE코드", example = "ROLE_MARKETING")
    private String roleName;

    @Schema(description = "연락처", example = "010-1234-1234")
    private String phone;

    @Schema(description = "이메일", example = "example@seobuk.kr")
    private String email;

    @Schema(description = "승인여부")
    private Boolean approved;

    @Schema(description = "생성일자")
    private LocalDateTime createDate;
}
