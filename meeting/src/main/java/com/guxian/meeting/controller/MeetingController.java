package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.vo.MeetingVo;
import com.guxian.meeting.service.MeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 对会议的操作
 */
@RestController
@RequestMapping("/meeting")
public class MeetingController {

    private MeetingService meetingService;

    public  MeetingController (@Autowired MeetingService meetingService) {
        this.meetingService = meetingService;
    }

    /**
     * 添加会议
     * @param meeting
     * @return
     */

    @PostMapping("/")
    public ResponseData createMeeting(@RequestBody @Validated(AddGroup.class) MeetingVo meeting) {
        return ResponseData.success()
                .data(meetingService.addMeeting(meeting.toMeeting())
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.CREATE_MEETING_FAILED)));
    }

    @PostMapping("/test2")
    public ResponseData test2(){
        return ResponseData.success("1", "2");
    }



    @PatchMapping("/")
    public ResponseData updateMeeting(@RequestBody @Validated(UpdateGroup.class)  MeetingVo meeting) {
        return ResponseData.success()
                .data(
                        meetingService.updateMeeting(meeting.toMeeting())
                                .orElseThrow(() -> new ServiceException(BizCodeEnum.UPDATE_MEETING_FAILED)));
    }


    @DeleteMapping("/{id}")
    public ResponseData deleteMeeting(@PathVariable("id") Long id) {
        return ResponseData.is(meetingService.removeById(id));
    }


    @GetMapping("/{id}")
    public ResponseData getMeeting(@PathVariable("id") Long id) {
        return ResponseData.success()
                .data(meetingService.getMeetingById(id)
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.MEETING_NOT_EXIST)));
    }

    @GetMapping("/")
    public ResponseData getMeetingByName(String name) {
        return ResponseData.success()
                .data(meetingService.getMeetingByName(name).orElseThrow(() -> new ServiceException(BizCodeEnum.MEETING_NOT_EXIST)));
    }

    @GetMapping("/list")
    public ResponseData getMeetingList(int page, int size) {
        return ResponseData.success()
                .data(meetingService.getAll(page, size));
    }


}


