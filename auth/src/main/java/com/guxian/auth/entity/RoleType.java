package com.guxian.auth.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
public enum RoleType {
    //普通用户
    ROLE_USER(2,"普通用户"),
    //管理员
    ROLE_ADMIN(5,"管理员"),
    //游客
    ROLE_GUEST(1,"游客");

    @EnumValue
    @JsonValue
    private final Integer explain;
    private final String describe;
}
