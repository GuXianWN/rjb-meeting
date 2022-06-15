package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.vo.MeetingCheckVo;
import com.guxian.meeting.service.MeetingCheckService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 对会议签到进行操作
 */
@RestController
@RequestMapping("/meeting/check")
public class MeetingCheckController {

    private final MeetingCheckService meetingCheckService;

    public MeetingCheckController(MeetingCheckService meetingCheckService) {
        this.meetingCheckService = meetingCheckService;
    }

    /**
     * smjb
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseData getMeetingCheckById(@PathVariable String id) {
        return ResponseData.success(meetingCheckService.getById(id));
    }

    /**
     * 添加会议签到
     * @param meetingCheck
     * @param code
     * @return
     */
    @PostMapping("/{code}")
    public ResponseData createMeetingCheck(@RequestBody @Validated(AddGroup.class) MeetingCheckVo meetingCheck, @PathVariable String code) {
        return ResponseData.success(meetingCheckService.addCheckType(meetingCheck.toMeetingCheck(), code));
    }


    @PutMapping("/{id}")
    public ResponseData updateMeetingCheck(@PathVariable Long id, @RequestBody @Validated(value = {UpdateGroup.class}) MeetingCheckVo meetingCheck) {
        return ResponseData.success(meetingCheckService.updateById(meetingCheck.toMeetingCheck().setMeetingId(id)));
    }


    @GetMapping("/created-meeting/{uid}")
    public ResponseData listMeetingCheck(@PathVariable String uid) {
        return ResponseData.success(meetingCheckService.listByMap(Map.of("uid", uid)));
    }
}
