package com.guxian.meeting.controller;

import com.baomidou.mybatisplus.core.toolkit.ObjectUtils;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.utils.JwtUtils;
import com.guxian.common.utils.SomeUtils;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.vo.MeetingCheckVo;
import com.guxian.meeting.service.MeetingCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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
     * 获取会议的签到相关信息，通过会议id进行获取
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public ResponseData getMeetingCheckById(@PathVariable Long id) {
        var byId = meetingCheckService.listByMap(Map.of("meeting_id", id));
        return ResponseData.success(byId);
    }

    /**
     * 添加会议签到
     *
     * @param meetingCheck
     * @return
     */

    @PostMapping
    public ResponseData createMeetingCheck(@RequestBody @Validated(AddGroup.class) MeetingCheckVo meetingCheck, HttpServletRequest request) {
        Long uid = jwtUtils.getUid(request);
        if (SomeUtils.getNotNullValue(meetingCheck.getCode()
                , meetingCheck.getFaceUrl()) == null) {
            //todo 无签到方式的处理
        }

        String data = SomeUtils.<String>getNotNullValue(meetingCheck.getCode()
                , meetingCheck.getFaceUrl());
        return ResponseData.success().data(meetingCheckService.createMeetingCheck(meetingCheck.toMeetingCheck(), data,uid));
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


    @GetMapping("/list/types")
    public ResponseData getMeetingTypes() {
        var map = meetingCheckService.getMeetingTypeList();
        return ResponseData.success(map);
    }
}
