package com.guxian.meeting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CheckInfor {
    private MeetingCheck meetingCheck;
    private List<CheckIn> checkInList;
    private Integer JoinNum;
    private Integer checkNum;

    public CheckInfor(MeetingCheck meetingCheck) {
        this.meetingCheck = meetingCheck;
    }
}
