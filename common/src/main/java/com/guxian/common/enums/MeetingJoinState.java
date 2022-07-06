package com.guxian.common.enums;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum MeetingJoinState {
    Accepted(2),
    WHITELIST(3);

    @EnumValue
    Integer explain;
}
