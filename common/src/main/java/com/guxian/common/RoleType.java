package com.guxian.common;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public enum RoleType {
    //普通用户
    ROLE_USER("普通用户"),
    //管理员
    ROLE_ADMIN("管理员"),
    //游客
    ROLE_GUEST("游客");
    String expian;

}
