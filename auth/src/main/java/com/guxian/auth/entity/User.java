package com.guxian.auth.entity;


import com.baomidou.mybatisplus.annotation.EnumValue;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.util.Date;

import com.guxian.common.RoleType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.stereotype.Component;

/**
 * @TableName user
 */
@TableName(value = "user")
@Data
@Accessors(chain = true)
@Component
public class User {
    /**
     *
     */
    @TableId
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号
     */
    private String account;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    @TableField(value = "portrait_url")
    private String portraitUrl;

    /**
     * 手机号
     */
    private String mobile;

    /**
     * 状态
     */
    @EnumValue
    private Integer status;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;


    private Integer roleId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

    public User setRole(RoleType roleId) {
        this.roleId = roleId.getValue();
        return this;
    }

    public User setStatus(UserStatus status) {
        this.status = status.getValue();
        return this;
    }
}