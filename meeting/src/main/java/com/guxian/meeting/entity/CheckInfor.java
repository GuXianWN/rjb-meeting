package com.guxian.meeting.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CheckInfor {
    private MeetingCheck meetingCheck;
    private List<CheckIn> checkInList;
}
