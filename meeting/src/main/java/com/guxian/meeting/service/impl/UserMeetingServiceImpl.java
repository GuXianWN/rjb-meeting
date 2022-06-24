package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.CheckWay;
import com.guxian.common.UserCheckInStatus;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.UserMeeting;
import com.guxian.meeting.service.UserMeetingService;
import com.guxian.meeting.mapper.UserMeetingMapper;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 *
 */
@Service
@Setter(onMethod_ = @Autowired)
public class UserMeetingServiceImpl extends ServiceImpl<UserMeetingMapper, UserMeeting>
        implements UserMeetingService {
    UserMeetingMapper userMeetingMapper;
    @Autowired
    private JwtUtils jwtUtils;

    @Override
    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code) {
        return Optional.empty();
    }

    @Override
    public void checkIn(Long meetingId, CheckWay way) {
        List<UserMeeting> userMeetings = userMeetingMapper.queryByMidAndUid(meetingId, CurrentUserSession.getUserSession().getUserId());
        if (!userMeetings.isEmpty()) {
            throw new ServiceException(BizCodeEnum.CHECK_IN_ALREADY);
        }

        this.save(new UserMeeting()
                .setType(UserCheckInStatus.JOIN)
                .setMid(meetingId)
                .setJoinTime(Date.from(Instant.now()))
                .setUid(CurrentUserSession.getUserSession().getUserId()));
    }

    @Override
    public int removeUserByMeeting(Long mid, Long uid) {
        return baseMapper.delete(new LambdaQueryWrapper<>(new UserMeeting()
                .setMid(mid)
                .setUid(uid)));
    }

    @Override
    public List<UserMeeting> getUserByMeeting(Long mid) {
        return baseMapper.selectList(new LambdaQueryWrapper<>(new UserMeeting()
                .setMid(mid)));
    }

    @Override
    public UserMeeting joinMeeting(Long mid, Long uid) {
        UserMeeting userMeeting1 = baseMapper.selectOne(new LambdaQueryWrapper<UserMeeting>()
                .eq(UserMeeting::getUid, uid)
                .eq(UserMeeting::getMid, mid));
        if (userMeeting1!=null){
            throw new ServiceException(BizCodeEnum.JOINED);
        }

        UserMeeting userMeeting = new UserMeeting();
        userMeeting.setMid(mid)
                .setUid(uid)
                .setJoinTime(new Date());
        baseMapper.insert(userMeeting);
        return userMeeting;
    }
}




