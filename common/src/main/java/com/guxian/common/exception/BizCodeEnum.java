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
    OSS_INIT_EXCEPTION(10002, "服务器初始OSS异常"),

    CREATE_MEETING_FAILED(11001, "创建会议失败"),
    UPDATE_MEETING_FAILED(11002, "更新会议失败"),
    JOINED(11002, "已加入"),

    MEETING_NOT_EXIST(11003, "会议不存在"),

    CHECK_IN_CODE_NOT_EXIST(12001,"不存在签到码方式" ),
    CHECK_IN_CODE_ERROR(12002, "签到码错误"),
    CHECK_DOES_NOT_EXIST(12003,"签到不存在"),



    USER_EXIST_EXCEPTION(15001, "用户已存在"),
    PHONE_EXIST_EXCEPTION(15002, "手机号已存在"),
    LOGIN_ACCOUNT_PASSWORD_EXCEPTION(15003, "账号或密码错误"),
    GET_OAUTH_TOKEN_EXCEPTION(15004, "认证失败"),
    USER_NOT_EXIST(15005, "用户不存在"),
    NO_ACCESS(15006, "无权限访问"),
    CHECK_IN_FAIL(15007, "签到失败"),
    CHECK_IN_ALREADY(15008, "已签到"),
    NOT_LOGGED_IN(15009,"未登录"),
    USER_FACE_NOT_EXIST(15010, "该用户未上传脸部照片"),
    NO_FACE_WAS_DETECTED(15011, "未检测到面部图像"),
    FACE_CONTRAST_INCONSISTENT(15012, "脸部和数据库中不一致！尝试变化角度再试一次！"),

    ;


    private int code;
    private String msg;

    BizCodeEnum(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }
}

