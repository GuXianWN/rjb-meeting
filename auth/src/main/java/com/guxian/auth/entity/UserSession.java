package com.guxian.auth.entity;


import com.guxian.common.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.servlet.http.HttpServletRequest;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSession {
    private Long userId;
    private String userName;
    private String role;
    private String token;
    private String ip;

    public static UserSession forUser(User user, HttpServletRequest request,String token){
        return new UserSession()
                .setUserId(user.getId())
                .setUserName(user.getUsername())
                .setRole(user.getRoleId().getDescribe())
                .setToken(token)
                .setIp(request.getRemoteAddr());
    }
}
