package com.guxian.meeting.service.impl;

import com.guxian.common.redis.RedisUtils;
import com.guxian.common.redis.config.RedisTemplateConfigurer;
import com.guxian.meeting.entity.MeetingCheck;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.redis.connection.RedisConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Date;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

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
}
