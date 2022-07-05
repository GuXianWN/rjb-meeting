package com.guxian.common;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public enum MeetingJoinState {
    InviteWaitingToBeAccepted(0),
    JoinWaitingToBeAccepted(1),
    Accepted(2),
    WHITELIST(3);

    Integer explain;
}
