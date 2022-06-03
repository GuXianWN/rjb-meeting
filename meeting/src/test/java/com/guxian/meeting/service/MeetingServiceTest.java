package com.guxian.meeting.service;

import com.guxian.meeting.entity.Meeting;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
class MeetingServiceTest {
    @Autowired
    private MeetingService meetingService;
    
    @Test
    @Transactional
    void addMeetingTest() {
        var res=meetingService.addMeeting(
                new Meeting()
                        .setName("test1")
                        .setBeginTime(Date.from(Instant.now()))
                        .setInstruction("test213")
                        .setEndTime(Date.from(Instant.now().minusSeconds(100L))));
        res.ifPresent(a -> assertNotNull(a.getId(), "添加会议失败"));
    }
}