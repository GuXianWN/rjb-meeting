package com.guxian.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;
import org.checkerframework.checker.units.qual.C;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

/**
 * @TableName user
 */
@TableName(value = "user")
@Data
@Accessors(chain = true)
@Component
public class User implements Serializable, UserDetails {
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
    private UserStatus status;

    /**
     *
     */
    @TableField(value = "create_time")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
//        return this.status.value.intValue() != UserStatus.FREEZE.value;
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
//        return this.status.value.intValue() != UserStatus.DELETE.value;
        return true;
    }

    @Override
    public boolean isEnabled() {
//        return this.status.value.intValue() != UserStatus.DELETE.value;
        return true;
    }
}