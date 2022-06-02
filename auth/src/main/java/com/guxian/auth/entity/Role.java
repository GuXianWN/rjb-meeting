package com.guxian.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName role
 */
@TableName(value ="role")
@Data
public class Role implements Serializable {
    /**
     * 
     */
    @TableId
    private Integer id;

    /**
     * 
     */
    @TableField(value ="role_level")
    private Integer roleLevel;

    /**
     * 
     */
    @TableField(value ="role_name")
    private String roleName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}