package com.guxian.common;

public enum CheckWay {
    /**
     * 人脸识别
     */
    FACE,

    /**
     * 密码识别
     */
    CODE,

    /**
     * 无需签到
     */
    NONE
    ;
    public int getValue() {
        return ordinal();
    }

}
