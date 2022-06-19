package com.guxian.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.meeting.entity.vo.UserVo;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

@Data
@AllArgsConstructor
@Accessors(chain = true)
@NoArgsConstructor
public class MeetingInfor {
    Long id;
    String title;
    //String avatar;
    String description;
    UserVo owner;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date startAt;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date endAt;
    //参加人数
    Integer attendTotalNumber;
    //已到人数
    Integer attendNumber;
    //全部参会人信息
    List<CheckInfor> attendDetail;
    //0待开始 1进行中 2已结束
    Integer status;
    //会议签到码
    String signinCode;

    public static MeetingInfor from(Meeting meeting) {
        MeetingInfor meetingInfor = new MeetingInfor();
        meetingInfor.setId(meeting.getId())
                .setTitle(meeting.getName())
                .setDescription(meeting.getInstruction())
                .setOwner(UserVo.from(meeting.getCreateUid()))
                .setStartAt(meeting.getBeginTime())
                .setEndAt(meeting.getEndTime())
                .setStatus(meeting.getState());
        return meetingInfor;
    }
}
