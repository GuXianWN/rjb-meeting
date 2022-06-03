package com.guxian.meeting.entity.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.common.CheckWay;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.MeetingCheck;
import lombok.Data;

import javax.validation.constraints.Future;
import javax.validation.constraints.FutureOrPresent;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import java.io.Serializable;
import java.util.Date;

/**
 * 会议发起的签到记录，统计每一场会议发起的签到，主要解决的是一场会议发起了多次签到的问题
 *
 * @TableName meeting_check
 */
@Data
public class MeetingCheckVo implements Serializable {

    @NotNull(message = "会议签到id不能为空", groups = {UpdateGroup.class})
    private Long id;

    @NotNull(message = "会议id不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private Long meetingId;

    /**
     * 签到方式
     */
    @NotNull(message = "签到方式不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private CheckWay checkWay;

    /**
     * 该签到方式的 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    @FutureOrPresent(message = "签到开始时间必须是未来时间", groups = {AddGroup.class})
    private Date beginTime;

    /**
     * 该签到方式的截至时间
     */

    @FutureOrPresent(message = "签到截至时间必须是未来时间", groups = {AddGroup.class})
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    public MeetingCheck toMeetingCheck() {
        return  new MeetingCheck()
                .setId(this.id)
                .setMeetingId(this.meetingId)
                .setCheckWay(this.checkWay.getValue())
                .setBeginTime(this.beginTime)
                .setEndTime(this.endTime);

    }
}