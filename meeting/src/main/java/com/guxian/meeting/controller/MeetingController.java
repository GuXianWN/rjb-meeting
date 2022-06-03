package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.valid.AddGroup;
import com.guxian.meeting.entity.vo.MeetingVo;
import com.guxian.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/meeting")
public class MeetingController {

    private MeetingService meetingService;

    public  MeetingController (@Autowired MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    @PostMapping("/create")
    public ResponseData createMeeting(@RequestBody @Validated(AddGroup.class) MeetingVo meeting) {
        return ResponseData.success()
                .data(meetingService.addMeeting(meeting.toMeeting())
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.CREATE_MEETING_FAILED)));
    }

    @PostMapping("/test2")
    public ResponseData test2(){
        return ResponseData.success("1", "2");
    }


    @PatchMapping("/update")
    public ResponseData updateMeeting(@RequestBody MeetingVo meeting) {
        return ResponseData.success()
                .data(
                        meetingService.updateMeeting(meeting.toMeeting())
                                .orElseThrow(() -> new ServiceException(BizCodeEnum.UPDATE_MEETING_FAILED)));
    }

    @GetMapping("/test")
    public String test(){
        return "1wedwewfwrf";
    }
}


