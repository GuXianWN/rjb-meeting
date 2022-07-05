package com.guxian.auth.entity;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

@Getter
@AllArgsConstructor
@Slf4j
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

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String describe;

    public Integer getValue() {
        return ordinal();
    }
}
