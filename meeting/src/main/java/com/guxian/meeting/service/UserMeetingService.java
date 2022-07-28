package com.guxian.meeting.service;

import com.guxian.common.enums.CheckWay;
import com.guxian.common.entity.PageData;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.UserMeeting;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.meeting.entity.vo.UserMeetingVo;

import java.util.List;
import java.util.Optional;

/**
 *
 */
public interface UserMeetingService extends IService<UserMeeting> {
    Optional<MeetingCheck> addCheckType(MeetingCheck meetingCheck, String code);

    void checkIn(Long meetingId, CheckWay way);

    int removeUserByMeeting(Long mid, Long uid);

    List<UserMeeting> getUserByMeeting(Long mid);

    PageData getMeetingJoinList(Long uid, Long page, Long size);

    UserMeeting selectById(Long umid);

    PageData list(Long page, Long size);

    /**
     * 加入会议 (需要白名单)
     */
    UserMeeting joinMeeting(Long mid, Long uid);

    /**
     * 添加会议白名单
     */
    void addWhiteListed(List<Long> list, Long mid);

    void deleteWhiteListed(Long uid, Long mid);

    void deleteByMid(Long meeting);

    void quit(Long mid);

    List<UserMeetingVo> whiteListedList(Long mid);
}
