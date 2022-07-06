package com.guxian.common.enums;

public enum CheckWay {

    /**
     * 无需签到
     */
    NONE,
    /**
     * 人脸识别
     */
    FACE,

    /**
     * 密码识别
     */
    CODE,

    ;
    public int getValue() {
        return ordinal();
    }

}
