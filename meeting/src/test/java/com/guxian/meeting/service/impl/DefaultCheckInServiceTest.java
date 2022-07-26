package com.guxian.meeting.service.impl;

import com.guxian.common.redis.RedisUtils;
import com.guxian.common.redis.config.RedisTemplateConfigurer;
import com.guxian.common.utils.SomeUtils;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.vo.CheckDataVo;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@Log4j2
class DefaultCheckInServiceTest {

    private DefaultCheckInService defaultCheckInServiceUnderTest;
    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private MeetingCheckServiceImpl meetingCheckService;

    @BeforeEach
    void setUp() {
//        defaultCheckInServiceUnderTest = new DefaultCheckInService(userMeetingService);
        RedisUtils.ops = new RedisTemplateConfigurer().redisTemplateForStringObject(this.redisConnectionFactory).opsForValue();
    }
}
