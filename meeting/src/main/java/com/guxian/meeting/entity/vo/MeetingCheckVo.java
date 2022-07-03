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
 * 会议发起的签到记录，统计每一场会议发起的签到，
 * 主要解决的是一场会议发起了多次签到的问题
 *  传入参数
 *
 * @TableName meeting_check
 */
@Data
public class MeetingCheckVo implements Serializable {

    @NotNull(message = "会议签到id不能为空", groups = {UpdateGroup.class})
    private Long id;

    /**
     * 签到方式
     */
    @NotNull(message = "签到方式不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private CheckWay checkWay;

    /**
     * 该签到方式的截至时间
     */
    @NotNull(message = "持续时间不能为空", groups = {UpdateGroup.class, AddGroup.class})
    private Integer duration;


    /**
     * 如果使用CheckWay.CODE，则需要传入code
     */
    private String code;

    /**
     * 如果使用CheckWay.FACE，则需要传入face的url
     */
    private String faceUrl;

    public MeetingCheck toMeetingCheck() {
        return new MeetingCheck()
                .setMeetingId(this.id)
                .setCheckWay(this.checkWay.getValue())
                .setDuration(duration);
    }
}