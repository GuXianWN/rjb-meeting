package com.guxian.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum RoleType {
    //普通用户
    ROLE_USER(2),
    //管理员
    ROLE_ADMIN(5),
    //游客
    ROLE_GUEST(1);
    Integer explain;

}
