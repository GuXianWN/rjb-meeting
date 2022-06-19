package com.guxian.meeting.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.utils.JwtUtils;
import com.guxian.common.utils.SomeUtils;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.vo.MeetingCheckVo;
import com.guxian.meeting.service.MeetingCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * 对会议签到进行操作
 */
@RestController
@RequestMapping("/meeting/check")
public class MeetingCheckController {
    @Autowired
    private JwtUtils jwtUtils;
    private final MeetingCheckService meetingCheckService;

    public MeetingCheckController(MeetingCheckService meetingCheckService) {
        this.meetingCheckService = meetingCheckService;
    }

    /**
     * smjb
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseData getMeetingCheckById(@PathVariable String id) {
        return ResponseData.success(meetingCheckService.getById(id));
    }

    /**
     * 添加会议签到
     *
     * @param meetingCheck
     * @return
     */

    @PostMapping("/")
    public ResponseData createMeetingCheck(@RequestBody @Validated(AddGroup.class) MeetingCheckVo meetingCheck) {
        if (SomeUtils.getNotNullValue(meetingCheck.getCode()
                , meetingCheck.getFaceUrl()) == null) {
            //todo 无签到方式的处理
        }

        String code = meetingCheck.getCode();
        String data = SomeUtils.<String>getNotNullValue(meetingCheck.getCode()
                , meetingCheck.getFaceUrl());
        return ResponseData.success().data(meetingCheckService.createMeetingCheck(meetingCheck.toMeetingCheck(), data));
    }

    /**
     * 签到码签到
     *
     * @param id
     * @param meetingCheck
     * @return
     */


    @PutMapping("/{id}")
    public ResponseData updateMeetingCheck(@PathVariable Long id, @RequestBody @Validated(value = {UpdateGroup.class}) MeetingCheckVo meetingCheck) {
        return ResponseData.success(meetingCheckService.updateById(meetingCheck.toMeetingCheck().setMeetingId(id)));
    }

    @GetMapping("/created-meeting/{uid}")
    public ResponseData listMeetingCheck(@PathVariable String uid) {
        return ResponseData.success(meetingCheckService.listByMap(Map.of("uid", uid)));
    }
}
