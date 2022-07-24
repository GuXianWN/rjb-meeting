package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
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

    public CheckInController(CheckInService checkInService) {
        this.checkInService = checkInService;
    }

    /**
     *
     * @param checkDataVo 签到对象
     * @return 签到结果
     */
    @PostMapping
    public ResponseData check(
            @Validated CheckDataVo checkDataVo) {
        if(checkInService.checkIn(checkDataVo)){
            return ResponseData.success();
        }
        return ResponseData.error(BizCodeEnum.CHECK_IN_FAIL);
    }

    @PostMapping("/reCheck")
    public ResponseData reCheck(@RequestBody ReCheckVo reCheckVo) {
        Long uid = CurrentUserSession.getUserSession().getUserId();
        checkInService.reCheck(reCheckVo, uid);
        return ResponseData.success();
    }


}
