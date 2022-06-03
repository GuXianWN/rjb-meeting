package com.guxian.meeting.controller;

import com.guxian.common.entity.ResponseData;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.mapper.CheckInMapper;
import com.guxian.meeting.service.CheckInService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.loadbalancer.RequestData;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@NoArgsConstructor
@RequestMapping("/checkin")
public class MeetingCheckInController {
    private CheckInService checkInMapper;


    @Autowired
    public MeetingCheckInController(CheckInService checkInMapper){
        this.checkInMapper = checkInMapper;
    }

    @PostMapping("/checkin/{meetingId}")
    public ResponseData checkIn(@PathVariable("meetingId") Long meetingId){
        return ResponseData.success(checkInMapper.checkIn(meetingId));
    }


}
