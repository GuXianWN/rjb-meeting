package com.guxian.meeting.service;

import com.guxian.common.entity.PageData;
import com.guxian.meeting.entity.CheckInfor;
import com.guxian.meeting.entity.MeetingCheck;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 *
 */
public interface MeetingCheckService extends IService<MeetingCheck> {


    Optional<MeetingCheck> createMeetingCheck(MeetingCheck toMeetingCheck, String data,Long uid);

    List<CheckInfor> getCheckInList(Long id);

    MeetingCheck getCheckById(Long checkId);

    Map<Integer,Long> getMeetingTypeList();


}
