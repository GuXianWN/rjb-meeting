package com.guxian.meeting.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

/**
 * 
 * @TableName check_in
 */
@TableName(value ="check_in")
@Data
public class CheckIn implements Serializable {
    /**
     * 
     */
    @TableId
    private Long id;

    /**
     * 签到时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date checkTime;

    /**
     * 用户id
     */
    private Long uid;

    /**
     * 签到id
     */
    private Long cid;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}