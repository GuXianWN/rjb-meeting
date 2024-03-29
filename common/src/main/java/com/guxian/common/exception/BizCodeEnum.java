package com.guxian.common.exception;

import lombok.Getter;

/**
 * 11:会议
 * 12:签到
 * 15:用户
 */
@Getter
public enum BizCodeEnum {
    UNKNOW_EXCEPTION(10000, "未知异常"),
    VALID_EXCEPTION(10001, "参数校验异常"),

    USER_EXIST_EXCEPTION(15001, "用户已存在"),
    PHONE_EXIST_EXCEPTION(15002, "手机号已存在"),
    LOGINACCT_PASSWORD_EXCEPTION(15003, "账号或密码错误"),
    GET_OAUTH_TOKEN_EXCEPTION(15004, "认证失败");

    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

