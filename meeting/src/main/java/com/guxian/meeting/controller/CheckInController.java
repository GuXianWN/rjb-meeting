package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.utils.JwtUtils;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.entity.vo.ReCheckVo;
import com.guxian.meeting.service.CheckInService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 用户签到操作
 */
@RestController
@RequestMapping("/check-in")
public class CheckInController {
    private final CheckInService checkInService;
    @Autowired
    private JwtUtils jwtUtils;

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    @PostMapping
    public ResponseData check(
            @RequestBody @Validated CheckDataVo checkDataVo) {
        if (checkDataVo.getMeetingId() != null) {
            return ResponseData.error("会议ID不能为空");
        }
        return ResponseData.is(checkInService.checkIn(checkDataVo));
    }

    @PostMapping("/reCheck")
    public ResponseData reCheck(@RequestBody ReCheckVo reCheckVo, HttpServletRequest request){
        Long uid = jwtUtils.getUid(request);
        checkInService.reCheck(reCheckVo,uid);
        return  ResponseData.success();
    }
}
