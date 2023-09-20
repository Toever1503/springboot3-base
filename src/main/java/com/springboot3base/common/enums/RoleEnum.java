package com.springboot3base.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum RoleEnum {
    ROLE_ADMIN(1, "ROLE_ADMIN", "admin of system"),
    ROLE_USER(1, "ROLE_USER", "user of system");

    private Integer id;
    private String title;
    private String desc;
}
