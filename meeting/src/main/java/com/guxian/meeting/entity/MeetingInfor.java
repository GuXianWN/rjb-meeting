package com.guxian.meeting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.common.enums.MeetingState;
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
    String name;
    //String avatar;
    String explain;
    UserVo owner;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date beginTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    Date endTime;
    //参加人数
    Integer attendTotalNumber;
    //已到人数
    Integer attendNumber;
    //全部参会人信息
    List<CheckInfor> attendDetail;
    //0待开始 1进行中 2已结束
    MeetingState status;
    //会议签到码
    String signinCode;

    public static MeetingInfor from(Meeting meeting) {
        MeetingInfor meetingInfor = new MeetingInfor();
        meetingInfor.setId(meeting.getId())
                .setName(meeting.getName())
                .setExplain(meeting.getInstruction())
                .setOwner(UserVo.from(meeting.getCreateUid()))
                .setBeginTime(meeting.getBeginTime())
                .setEndTime(meeting.getEndTime())
                .setStatus(meeting.getState());
        return meetingInfor;
    }
}
