package com.guxian.meeting.controller;

import com.guxian.common.entity.R;
import com.guxian.meeting.entity.vo.MeetingVo;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/meeting")
public class MeetingController {
    @PostMapping("/create")
    public R createMeeting(@RequestBody MeetingVo meeting) {
        return R.success().data("data",meeting);
    }
}


