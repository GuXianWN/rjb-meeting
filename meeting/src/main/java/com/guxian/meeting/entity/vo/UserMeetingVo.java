package com.guxian.meeting.entity.vo;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.common.enums.MeetingJoinState;
import com.guxian.meeting.entity.UserMeeting;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserMeetingVo {
    private Long id;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 会议id
     */
    private Long mid;

    /**
     * 签到状态
     */
    private String type;

    /**
     * 加入时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date joinTime;

    /**
     * 退出时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date exitTime;

    /**
     * 加入状态
     */
    private MeetingJoinState joinState;

    private UserVo user;

    public static UserMeetingVo foUserMeeting(UserMeeting userMeeting){
        UserMeetingVo userMeetingVo = new UserMeetingVo();
        userMeetingVo.setId(userMeeting.getId());
        userMeetingVo.setUid(userMeeting.getUid());
        userMeetingVo.setMid(userMeeting.getMid());
        userMeetingVo.setType(userMeeting.getType());
        userMeetingVo.setJoinTime(userMeeting.getJoinTime());
        userMeetingVo.setExitTime(userMeeting.getExitTime());
        userMeetingVo.setJoinState(userMeeting.getJoinState());
        return userMeetingVo;
    }
}
