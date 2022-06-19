package com.guxian.meeting.service.impl;

import com.guxian.common.entity.UserSession;
import com.guxian.meeting.entity.Meeting;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.Instant;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class MeetingServiceImplTest {

    private MeetingServiceImpl meetingServiceImplUnderTest;

    @BeforeEach
    void setUp() {
        meetingServiceImplUnderTest = new MeetingServiceImpl();
        meetingServiceImplUnderTest.user = mock(UserSession.class);
    }

    @Test

    void testAddMeeting() {

        var userSession=Mockito.mock( Meeting.class);
        Meeting meeting1 = new Meeting().setCreateUid(1L).setInstruction("this is test");
        Mockito.when(userSession.setCreateUid(null)).thenReturn(meeting1);

        // Setup
        final Meeting meeting = new Meeting(0L, 0L, "name", "instruction",
                null,
                Date.from(Instant.now()),
                Date.from(Instant.now().plusSeconds(10000)), 0);
        final Optional<Meeting> expectedResult = Optional.of(
                new Meeting(0L, 0L, "name", "instruction", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0));
        when(meetingServiceImplUnderTest.user.getUserId()).thenReturn(0L);

        // Run the test
        final Optional<Meeting> result = meetingServiceImplUnderTest.addMeeting(meeting);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testUpdateMeeting() {
        // Setup
        final Meeting toMeeting = new Meeting(0L, 0L, "name", "instruction",
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0);
        final Optional<Meeting> expectedResult = Optional.of(
                new Meeting(0L, 0L, "name", "instruction", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0));

        // Run the test
        final Optional<Meeting> result = meetingServiceImplUnderTest.updateMeeting(toMeeting);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetMeetingById() {
        // Setup
        final Optional<Meeting> expectedResult = Optional.of(
                new Meeting(0L, 0L, "name", "instruction", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0));

        // Run the test
        final Meeting result = meetingServiceImplUnderTest.getMeetingById(0L);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetMeetingByName() {
        // Setup
        final Optional<Meeting> expectedResult = Optional.of(
                new Meeting(0L, 0L, "name", "instruction", new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0));

        // Run the test
        final Optional<Meeting> result = meetingServiceImplUnderTest.getMeetingByName("name");

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }

    @Test
    void testGetAll() {
        // Setup
        final List<Meeting> expectedResult = List.of(
                new Meeting(0L, 0L, "name", "instruction",
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(),
                        new GregorianCalendar(2020, Calendar.JANUARY, 1).getTime(), 0));

        // Run the test
        final List<Meeting> result = meetingServiceImplUnderTest.getAll(0, 0);

        // Verify the results
        assertThat(result).isEqualTo(expectedResult);
    }
}
