package com.guxian.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.guxian.common.config.RedisPrefix;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 会议发起的签到记录，统计每一场会议发起的签到，主要解决的是一场会议发起了多次签到的问题
 *
 * @TableName meeting_check
 */
@TableName(value = "meeting_check")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class MeetingCheck implements Serializable {
    /**
     *
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     *
     */
    private Long meetingId;

    /**
     * 签到方式
     */
    private Integer checkWay;

    /**
     * 该签到方式的 持续时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;

    /**
     * 该签到方式的截至时间
     */
    private Integer duration;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    @TableField(exist = false)
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String code;

    public static String buildKey(Long meetingId) {
        return RedisPrefix.CHECK_IN_CODE_PREFIX  + meetingId;
    }
}