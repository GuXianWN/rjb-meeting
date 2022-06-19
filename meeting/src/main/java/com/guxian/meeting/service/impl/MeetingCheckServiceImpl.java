package com.guxian.meeting.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.CheckWay;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.redis.RedisUtils;
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

    @Override
    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code) {
        var seconds = meetingCheck.getDuration();
        this.save(meetingCheck);
        RedisUtils.ops.set(MeetingCheck.buildKey(meetingCheck.getMeetingId()),
                code == null ? SomeUtils.randomString(6) : code, //默认随机6位字符串 作为签到码 如果传入了签到码则使用传入的签到码
                seconds, TimeUnit.MILLISECONDS);
        return Optional.of(meetingCheck);
    }

    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck) {
        return addCheckType(meetingCheck, null);
    }

    @Override
    public MeetingCheck createMeetingCheck(MeetingCheck toMeetingCheck, Long uid) {
        Meeting meeting = meetingService.getMeetingById(toMeetingCheck.getMeetingId());
        if (!meeting.getCreateUid().equals(uid)) {
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }
        toMeetingCheck.setBeginTime(new Date());
        baseMapper.insert(toMeetingCheck);
        RedisUtils.ops.set("meeting_check:" + toMeetingCheck.getCheckWay() + ":" + toMeetingCheck.getId(),
                JSON.toJSONString(toMeetingCheck),
                toMeetingCheck.getDuration(), TimeUnit.SECONDS);

        if (toMeetingCheck.getCheckWay() == CheckWay.CODE.getValue()) {
            RedisUtils.ops.set("meeting_check:check_code:" + toMeetingCheck.getId(), SomeUtils.randomCode(),
                    toMeetingCheck.getDuration(), TimeUnit.SECONDS);
        }
        return toMeetingCheck;
    }
}




