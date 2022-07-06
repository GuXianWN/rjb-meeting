package com.guxian.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum MeetingState {
    WAIT_TO_START(0, "等待开始"),
    PROCESSING(1, "进行中"),
    OVER(2, "结束");

    @EnumValue
    @JsonValue
    private final Integer explain;

    private final String dis;
}
