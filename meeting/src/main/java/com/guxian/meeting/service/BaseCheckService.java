package com.guxian.meeting.service;

import com.guxian.meeting.entity.vo.CheckDataVo;

public interface BaseCheckService {
    public  boolean checkIn(CheckDataVo checkDataVo, Long meetingId);
}
