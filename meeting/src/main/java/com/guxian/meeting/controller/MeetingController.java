package com.guxian.meeting.controller;

import com.guxian.common.entity.RespondseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.valid.AddGroup;
import com.guxian.meeting.entity.vo.MeetingVo;
import com.guxian.meeting.service.MeetingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
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
    public RespondseData createMeeting(@RequestBody @Validated(AddGroup.class) MeetingVo meeting) {
        return RespondseData.success()
                .data(meetingService.addMeeting(meeting.toMeeting())
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.CREATE_MEETING_FAILED)));
    }


    @PostMapping("/update")
    public RespondseData updateMeeting(@RequestBody MeetingVo meeting) {
        return RespondseData.success()
                .data(
                        meetingService.updateMeeting(meeting.toMeeting())
                                .orElseThrow(() -> new ServiceException(BizCodeEnum.UPDATE_MEETING_FAILED)));
    }
}


