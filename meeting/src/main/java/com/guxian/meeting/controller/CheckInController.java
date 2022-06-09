package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.service.CheckInService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/check-in")
@Setter(onMethod_ = @Autowired)
public class CheckInController {
    private CheckInService checkInService;

    @GetMapping
    public ResponseData check(
            @RequestBody @Validated CheckDataVo checkDataVo
    ) {
        if (checkDataVo.getMeetingId() == null) {
            return ResponseData.error("会议ID不能为空");
        }

        var check = checkInService.checkIn(checkDataVo);

        if (check) {
            return ResponseData.success("签到成功");
        }
        return ResponseData.success("签到失败");
    }

}
