package com.guxian.meeting.controller;

import com.guxian.common.entity.RespondseData;
import com.guxian.meeting.entity.vo.MeetingVo;
import com.guxian.meeting.service.MeetingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meeting")
@Setter(onMethod_ = @Autowired)
public class MeetingController {

    private MeetingService meetingService;

    @PostMapping("/create")
    public RespondseData createMeeting(@RequestBody MeetingVo meeting) {
        meetingService.save(meeting);
        return RespondseData.success().data(meeting);
    }


    @PostMapping("/update")
    public RespondseData updateMeeting(@RequestBody MeetingVo meeting) {
        return RespondseData.success().data(meeting);
    }


}


