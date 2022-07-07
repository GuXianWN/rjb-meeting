package com.guxian.meeting.entity.vo;

import com.guxian.common.enums.CheckWay;
import com.guxian.common.valid.AddGroup;
import com.guxian.common.valid.UpdateGroup;
import com.guxian.meeting.entity.MeetingCheck;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

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
     * 如果使用CheckWay.CODE，则需要传入code 或者自动生成
     */
    private String code;


    public MeetingCheck toMeetingCheck() {
        return new MeetingCheck()
                .setMeetingId(this.id)
                .setCheckWay(this.checkWay.getValue())
                .setDuration(duration);
    }
}