package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.CheckWay;
import com.guxian.common.UserCheckInStatus;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.UserMeeting;
import com.guxian.meeting.service.UserMeetingService;
import com.guxian.meeting.mapper.UserMeetingMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */
@Service
public class UserMeetingServiceImpl extends ServiceImpl<UserMeetingMapper, UserMeeting>
    implements UserMeetingService{

    @Override
    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code) {
        return Optional.empty();
    }

    @Override
    public void checkIn(Long meetingId, CheckWay code) {
        this.save(new UserMeeting()
                .setType(UserCheckInStatus.JOIN)
                .setMid(meetingId)
                .setUid(CurrentUserSession.getUserSession().getUserId()));
    }
}




