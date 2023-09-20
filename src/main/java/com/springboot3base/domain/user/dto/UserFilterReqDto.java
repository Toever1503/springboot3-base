package com.springboot3base.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class UserFilterReqDto {

    @Schema(name = "name", description = "이름")
    private String name;
    @Schema(name = "phone", description = "연락처")
    private String phone;
    @Schema(name = "email", description = "이메일")
    private String email;
    @Schema(name = "role", description = "ROLE")
    private String role;
    @Schema(name = "approved", description = "승인여부")
    private Boolean approved;
}
