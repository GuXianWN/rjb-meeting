package com.guxian.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MeetingJoinType {
    DEFAULT(0, "默认"),
    WHITELIST(1, "白名单加入");

    @EnumValue
    @JsonValue
    private final Integer value;
    private final String explain;
}
