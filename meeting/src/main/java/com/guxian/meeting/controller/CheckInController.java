package com.guxian.meeting.controller;

import com.guxian.common.CheckWay;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.Meeting;
import com.guxian.meeting.entity.vo.CheckDataVo;
import com.guxian.meeting.entity.vo.MeetingCheckVo;
import com.guxian.meeting.entity.vo.MeetingVo;
import com.guxian.meeting.service.CheckInService;
import com.guxian.meeting.service.MeetingService;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/check-in")
@Setter(onMethod_ = @Autowired)
public class CheckInController {
    private CheckInService checkInService;
    private MeetingService meetingService;

    @RequestMapping("/")
    public ResponseData check(
            @RequestBody @Validated CheckDataVo checkDataVo
    ) {

        if(checkDataVo.getMeetingId()==null){
            return ResponseData.error("会议ID不能为空");
        }

        checkInService.saveBatch(null);
        var check = checkInService.checkIn(checkDataVo);

        if (check) {
            return ResponseData.success("签到成功");
        }
        return ResponseData.success("签到失败");
    }
}
