package com.guxian.meeting.service.impl;

import com.guxian.common.CheckWay;
import com.guxian.common.exception.ServiceException;
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
        defaultCheckInServiceUnderTest = new DefaultCheckInService();
        RedisUtils.ops = new RedisTemplateConfigurer().redisTemplateForStringObject(this.redisConnectionFactory).opsForValue();
    }

    @Test
    void testCheckInWillThrowException() {
        // Setup
        final CheckDataVo checkDataVo = new CheckDataVo(0L, "code", "faceUrl", CheckWay.CODE);

        assertThatThrownBy(() -> defaultCheckInServiceUnderTest.checkIn(checkDataVo))
                .isInstanceOf(ServiceException.class)
                .hasMessageContaining("签到码不存在");
    }

    @Test
    @Transactional
    void testCheckInUseCodeWillReturnTrue() {

        var code = SomeUtils.randomString(6);

        // Setup
        final CheckDataVo checkDataVo = new CheckDataVo(0L, code, null, CheckWay.CODE);


        //add code to redis
        Instant beginTime = Instant.now();
        Instant endTime = Instant.now().plusSeconds(1000);

        final MeetingCheck meetingCheck = new MeetingCheck(0L, 0L, 0,
                Date.from(beginTime),
                endTime.getNano());

        // Run the test
        final Optional<MeetingCheck> result = meetingCheckService.createMeetingCheckUseCode(meetingCheck, code);

        // Verify the results
        assertThat(result.get().getMeetingId()).isNotNull();


        // Run the test
        final boolean result2 = defaultCheckInServiceUnderTest.checkInUseCode(checkDataVo.setCode(code).setMeetingId(result.get().getMeetingId()));


        log.info("result2:{} and checkDataVo code is {}", result2, checkDataVo.getCode());
        // Verify the results
        assertThat(result2).isTrue();
    }
}
