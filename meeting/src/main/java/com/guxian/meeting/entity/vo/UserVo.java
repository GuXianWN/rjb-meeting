package com.guxian.meeting.entity.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserVo {
    private Long id;

    /**
     * 用户名
     */
    private String username;

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
    private String portraitUrl;

    /**
     * 手机号
     */
    private String mobile;


    public static UserVo from(Long uid) {
        UserVo userVo = new UserVo();
        userVo.setId(uid);
        return userVo;
    }
}
