package com.guxian.meeting.service;

import com.guxian.common.CheckWay;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.UserMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.meeting.entity.vo.CheckDataVo;

import java.util.Optional;

/**
 *
 */
public interface UserMeetingService extends IService<UserMeeting> {
    Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code);

    void checkIn(Long meetingId, CheckWay code);
}
