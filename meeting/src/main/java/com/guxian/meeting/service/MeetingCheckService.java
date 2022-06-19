package com.guxian.meeting.service;

import com.guxian.meeting.entity.CheckInfor;
import com.guxian.meeting.entity.MeetingCheck;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface MeetingCheckService extends IService<MeetingCheck> {
    MeetingCheck createMeetingCheck(MeetingCheck toMeetingCheck,Long uid);

    List<CheckInfor> getCheckInList(Long id);
//    Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck,String code);
}
