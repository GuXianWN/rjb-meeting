package com.guxian.meeting.service.impl;

import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.CheckWay;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.redis.RedisUtils;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.SomeUtils;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.service.MeetingCheckService;
import com.guxian.meeting.mapper.MeetingCheckMapper;
import com.guxian.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Date;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class MeetingCheckServiceImpl extends ServiceImpl<MeetingCheckMapper, MeetingCheck>
        implements MeetingCheckService {

    private final MeetingService meetingService;

    public MeetingCheckServiceImpl(@Autowired MeetingService meetingService) {
        this.meetingService = meetingService;
    }


    public Optional<MeetingCheck> createMeetingCheckUseCode(MeetingCheck meetingCheck, String data) {


        RedisUtils.ops.set("meeting_check:check_code:" + meetingCheck.getId(), data != null ? SomeUtils.randomString(4) : data,
                meetingCheck.getDuration(), TimeUnit.SECONDS);
        return Optional.of(meetingCheck);
    }

    public Optional<MeetingCheck> createMeetingCheckUseFace(MeetingCheck toMeetingCheck, String data) {
        RedisUtils.ops.set("meeting_check:" + toMeetingCheck.getCheckWay() + ":" + toMeetingCheck.getId(),
                JSON.toJSONString(toMeetingCheck),
                toMeetingCheck.getDuration(), TimeUnit.SECONDS);
        return Optional.of(toMeetingCheck);
    }

    @Override
    public Optional<MeetingCheck> createMeetingCheck(MeetingCheck meetingCheck, String data) {
        Meeting meeting = meetingService.getMeetingById(meetingCheck.getMeetingId());
        if (!meeting.getCreateUid().equals(CurrentUserSession.getUserSession().getUserId())) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }

        meetingCheck.setBeginTime(Date.from(Instant.now()));

        //已经存在的会议签到，则删除原有的签到，重新创建新的签到
        if (baseMapper.selectById(meetingCheck.getMeetingId()) != null) {
            baseMapper.deleteById(meetingCheck.getMeetingId());
        }
        baseMapper.insert(meetingCheck);


        /**
         * 创建签到 ,不同的签到方式，创建的签到不同
         */
        if (meetingCheck.getCheckWay() == CheckWay.CODE.getValue()) {
            return createMeetingCheckUseCode(meetingCheck, data);
        } else {
            return createMeetingCheckUseFace(meetingCheck, data);
        }
    }
}




