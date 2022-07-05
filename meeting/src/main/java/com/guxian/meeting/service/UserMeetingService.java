package com.guxian.meeting.service;

import com.guxian.common.CheckWay;
import com.guxian.common.entity.PageData;
import com.guxian.meeting.entity.MeetingCheck;
import com.guxian.meeting.entity.UserMeeting;
import com.baomidou.mybatisplus.extension.service.IService;

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

    /**
     * 用户接受邀请
     */
//    boolean acceptInvite(Long mid);


    /**
     * 加入会议
     */
//    UserMeeting joinMeeting(Long mid, Long uid);

    /**
     * 邀请别人加入
     */
//    void invite(List<Long> uids,Long mid);

    /**
     * 会议主持接受加入
     */
//    boolean acceptJoin(Long umid);

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
}
