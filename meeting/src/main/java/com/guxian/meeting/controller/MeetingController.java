package com.guxian.meeting.controller;

import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.service.MeetingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Setter(onMethod_ = @Autowired)
public class MeetingController {
    MeetingService meetingService;

    @PostMapping("/add-meeting")
    public  addMeeting(Meeting meeting) {
        meetingService.save(meeting);
    }
}
