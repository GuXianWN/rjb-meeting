package com.guxian.meeting.service;

import com.guxian.meeting.MeetingApplication;
import com.guxian.meeting.controller.MeetingController;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.mapper.MeetingMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Answers;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MockMvcBuilder;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MeetingApplication.class)
class ThrowErrorAndSuccessTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    MeetingService meetingService;


    @MockBean
    MeetingMapper mapper;


    @Test
    void useMapReturnErrorTest() throws Exception {
        assertNotNull(mockMvc);
/*        BDDMockito.given(meetingService
                        .addMeeting(any()))
                .willReturn(
                        Optional.ofNullable(new Meeting()
                                .setId(1L)));*/


        var requestBody =
                new ClassPathResource("/request.meeting/create-meeting/200-ok.json")
                        .getInputStream()
                        .readAllBytes();

        mockMvc.perform(
                        post("/meeting")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(requestBody))
                .andExpect(status().isOk());

    }
}
