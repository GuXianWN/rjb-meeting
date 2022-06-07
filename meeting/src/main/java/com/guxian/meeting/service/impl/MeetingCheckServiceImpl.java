package com.guxian.meeting.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.common.entity.RedisPrefix;
import com.guxian.common.redis.RedisUtils;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.service.MeetingCheckService;
import com.guxian.meeting.mapper.MeetingCheckMapper;
import org.springframework.data.redis.connection.RedisListCommands;
import org.springframework.data.redis.core.RedisConnectionUtils;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */
@Service
public class MeetingCheckServiceImpl extends ServiceImpl<MeetingCheckMapper, MeetingCheck>
    implements MeetingCheckService{

    @Override
    public Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck) {

        this.save(meetingCheck); //add uid use token
        return Optional.of(meetingCheck);
    }
}




