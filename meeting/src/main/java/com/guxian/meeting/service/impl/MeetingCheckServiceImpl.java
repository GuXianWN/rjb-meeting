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
    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck) {
        var ms = meetingCheck.getEndTime().toInstant().toEpochMilli() - Instant.now().toEpochMilli();
        this.save(meetingCheck);
        RedisUtils.ops.set(MeetingCheck.buildKey(meetingCheck.getMeetingId()),
                SomeUtils.randomString(6),
                ms, TimeUnit.MILLISECONDS);
        return Optional.of(meetingCheck);
    }
}




