package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.service.CheckInService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * 用户签到操作
 */
@RestController
@RequestMapping("/check-in")
public class CheckInController {
    private final CheckInService checkInService;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @GetMapping
    public ResponseData check(
            @RequestBody @Validated CheckDataVo checkDataVo) {
        if (checkDataVo.getMeetingId() != null) {
            return ResponseData.error("会议ID不能为空");
        }
        return ResponseData.is(checkInService.checkIn(checkDataVo));
    }
}
