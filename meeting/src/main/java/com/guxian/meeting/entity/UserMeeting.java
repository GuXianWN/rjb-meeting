package com.guxian.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.guxian.common.UserCheckInStatus;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName user_meeting
 */
@TableName(value ="user_meeting")
@Data
@Accessors(chain = true)
public class UserMeeting implements Serializable {
    /**
     * 
     */
    @TableId(type = IdType.AUTO)
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
    private Date joinTime;

    /**
     * 退出时间
     */
    private Date exitTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
    public UserMeeting setType(String type) {
        this.type = type;
        return this;
    }

    public UserMeeting setType(UserCheckInStatus type) {
        this.type = type.getValue();
        return this;
    }
}