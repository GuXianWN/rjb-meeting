package com.guxian.meeting.service.impl;

import com.guxian.common.CheckWay;
import com.guxian.meeting.entity.UserMeeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

class UserMeetingServiceImplTest {

    @Mock
    private UserMeetingServiceImpl userMeetingServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        userMeetingServiceImplUnderTest = spy(new UserMeetingServiceImpl());
    }

    @Test
    void testToCheckIn() {
        // Setup
        // Run the test
//        when(userMeetingServiceImplUnderTest.toCheckIn(1L, CheckWay.CODE)).thenCallRealMethod();
//        userMeetingServiceImplUnderTest.toCheckIn(0L, CheckWay.FACE);

        // Verify the results

    }
}
