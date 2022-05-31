package com.guxian.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName meeting
 */
@TableName(value ="meeting")
@Data
public class Meeting implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 会议创建者
     */
    private Integer createUid;

    /**
     * name
     */
    private String name;

    /**
     * 说明
     */
    private String explain;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 开始时间
     */
    private Date beginTime;

    /**
     * 签到方式
     */
    private Integer checkWay;

    /**
     * 结束时间
     */
    private Date endTime;

    /**
     * 状态
     */
    private Integer state;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}