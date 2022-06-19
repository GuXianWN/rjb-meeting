package com.guxian.meeting.service;

import com.guxian.common.entity.UserSession;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.service.impl.MeetingServiceImpl;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@Log4j2
@AutoConfigureWebMvc
@AutoConfigureMockMvc
class MyMeetingServiceTest {

    @MockBean
    private MeetingServiceImpl meetingService;


    @Autowired
    private MockMvc mockMvc;

//    @BeforeTestMethod
//    public void setUp() {
//        System.out.println("before");
//        MockitoAnnotations.openMocks(this);
//    }

    @Test
    @DisplayName("添加会议")
    @Transactional
    void addMeetingTest() {
        var currentUserSession = Mockito.mock(CurrentUserSession.class);

        Mockito.when(currentUserSession.getUserSession())
                .thenReturn(new UserSession().setUserId(1L));
        var res = meetingService.addMeeting(
                new Meeting()
                        .setName("test1")
                        .setBeginTime(Date.from(Instant.now()))
                        .setInstruction("test213")
                        .setEndTime(Date.from(Instant.now().minusSeconds(100L))),1L);

        assertNotNull(res.orElse(null), "添加会议失败");
    }


//    @Test
//    @Transactional
//    void getMeetingTest() {
//        var res = meetingService.getMeetingById(1L);
//        res.ifPresent(a -> assertEquals("test1", a.getName(), "获取会议失败"));
//    }
}