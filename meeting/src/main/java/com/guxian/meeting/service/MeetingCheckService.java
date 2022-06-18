package com.guxian.meeting.service;

import com.guxian.meeting.entity.MeetingCheck;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.Optional;

/**
 *
 */
public interface MeetingCheckService extends IService<MeetingCheck> {
    MeetingCheck createMeetingCheck(MeetingCheck toMeetingCheck,Long uid);
//    Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck,String code);
}
