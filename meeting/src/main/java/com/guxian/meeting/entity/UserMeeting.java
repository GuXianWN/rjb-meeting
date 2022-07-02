package com.guxian.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.common.UserCheckInStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName user_meeting
 */
@TableName(value ="user_meeting")
@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
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
    private Integer joinState;

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