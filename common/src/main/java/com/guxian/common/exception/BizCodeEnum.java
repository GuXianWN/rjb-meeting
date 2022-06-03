package com.guxian.common.exception;

import lombok.Getter;

/**
 * 11:会议
 * 12:签到
 * 15:用户
 */
@Getter
public enum BizCodeEnum {
    UNKNOWN_EXCEPTION(10000, "未知异常"),
    VALID_EXCEPTION(10001, "参数校验异常"),

    CREATE_MEETING_FAILED(11001, "创建会议失败"),
    UPDATE_MEETING_FAILED(11002, "更新会议失败"),

    USER_EXIST_EXCEPTION(15001, "用户已存在"),
    PHONE_EXIST_EXCEPTION(15002, "手机号已存在"),
    LOGIN_ACCOUNT_PASSWORD_EXCEPTION(15003, "账号或密码错误"),
    GET_OAUTH_TOKEN_EXCEPTION(15004, "认证失败"),
    USER_NOT_EXIST(15005, "用户不存在"),
    NO_ACCESS(15006, "无权限访问"),


    ;
    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    BizCodeEnum(String msg) {
        this.msg = msg;
    }
}

