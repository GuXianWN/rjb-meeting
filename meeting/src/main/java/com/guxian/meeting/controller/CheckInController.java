package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.entity.vo.ReCheckVo;
import com.guxian.meeting.service.CheckInService;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户签到操作
 */
@RestController
@RequestMapping("/meeting/check-in")
public class CheckInController {
    private final CheckInService checkInService;
    private final JwtUtils jwtUtils;

    public CheckInController(CheckInService checkInService, JwtUtils jwtUtils) {
        this.checkInService = checkInService;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping
    public ResponseData check(
            @Validated CheckDataVo checkDataVo) {
        return ResponseData.is(checkInService.checkIn(checkDataVo));
    }

    @PostMapping("/reCheck")
    public ResponseData reCheck(@RequestBody ReCheckVo reCheckVo, HttpServletRequest request) {
        Long uid = CurrentUserSession.getUserSession().getUserId();
        checkInService.reCheck(reCheckVo, uid);
        return ResponseData.success();
    }
}
