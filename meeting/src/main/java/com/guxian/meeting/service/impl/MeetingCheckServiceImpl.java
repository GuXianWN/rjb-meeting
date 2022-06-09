package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.redis.RedisUtils;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.SomeUtils;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.service.MeetingCheckService;
import com.guxian.meeting.mapper.MeetingCheckMapper;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class MeetingCheckServiceImpl extends ServiceImpl<MeetingCheckMapper, MeetingCheck>
        implements MeetingCheckService {

    @Override
    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code) {
        var ms = meetingCheck.getEndTime().toInstant().toEpochMilli() - Instant.now().toEpochMilli();
        this.save(meetingCheck);
        RedisUtils.ops.set(MeetingCheck.buildKey(meetingCheck.getMeetingId()),
                code == null ? SomeUtils.randomString(6) : code, //默认随机6位字符串 作为签到码 如果传入了签到码则使用传入的签到码
                ms, TimeUnit.MILLISECONDS);
        return Optional.of(meetingCheck);
    }


    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck) {
        return addCheckType(meetingCheck, null);
    }
}




