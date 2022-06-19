package com.guxian.meeting.service.impl;

import com.guxian.common.redis.RedisUtils;
import com.guxian.common.redis.config.RedisTemplateConfigurer;
import com.guxian.meeting.entity.MeetingCheck;
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
import static org.mockito.Mockito.spy;

@SpringBootTest
class MeetingCheckServiceImplTest {

    @Autowired
    private RedisConnectionFactory redisConnectionFactory;

    @Autowired
    private MeetingCheckServiceImpl meetingCheckService;


    @BeforeEach
    void setUp() {
        RedisUtils.ops = new RedisTemplateConfigurer().redisTemplateForStringObject(this.redisConnectionFactory).opsForValue();
        meetingCheckService = spy(meetingCheckService);
    }


    @Test
    @Transactional
    void testAddCheckType() {
        Instant beginTime = Instant.now();
        Instant endTime = Instant.now().plusSeconds(1000);
        // Setup
        final MeetingCheck meetingCheck = new MeetingCheck(null, 0L, 0,
                Date.from(beginTime),
                endTime.getNano());

        // Run the test
        final Optional<MeetingCheck> result = meetingCheckService.createMeetingCheck(meetingCheck,"code");

        // Verify the results
        assertThat(result.get().getMeetingId()).isNotNull();

    }
}
