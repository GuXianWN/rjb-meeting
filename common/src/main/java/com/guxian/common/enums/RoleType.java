package com.guxian.common.enums;

import lombok.AllArgsConstructor;
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


    public int getValue() {
        return ordinal();
    }

    public  static RoleType valueOf(Integer or) {
        for (var i : RoleType.values()) {
            if ((i.getValue() == or)) {
                return i;
            }
        }
        return null;
    }
}
