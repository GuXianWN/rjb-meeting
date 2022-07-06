package com.guxian.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.guxian.common.enums.MeetingJoinType;
import com.guxian.common.enums.MeetingState;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName meeting
 */
@TableName(value ="meeting")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Meeting implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会议创建者
     */
    private Long createUid;

    /**
     * 会议名称
     */
    private String name;

    /**
     * 说明
     */
    private String instruction;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 开始时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date beginTime;


    /**
     * 结束时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date endTime;

    /**
     * 状态
     */
    //0待开始 1进行中 2已结束
    private MeetingState state;

    /**
     * 会议加入
     */
    private MeetingJoinType joinType;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}