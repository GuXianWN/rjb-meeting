package com.guxian.meeting.service;

import com.guxian.common.CheckWay;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.UserMeeting;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface UserMeetingService extends IService<UserMeeting> {
    Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code);

    void checkIn(Long meetingId, CheckWay way);

    int removeUserByMeeting(Long mid, Long uid);

    List<UserMeeting> getUserByMeeting(Long mid);

    UserMeeting joinMeeting(Long mid, Long uid);
}
