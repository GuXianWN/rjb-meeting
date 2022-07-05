package com.guxian.auth.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public enum UserStatus {

    /**
     * 正常
     */
    NORMAL(0, "正常"),

    /**
     * 冻结
     */
    FREEZE(1, "冻结"),

    /**
     * 删除
     */
    DELETE(2, "删除");


    Integer value;
    String describe;

    public Integer getValue() {
        return ordinal();
    }
}
