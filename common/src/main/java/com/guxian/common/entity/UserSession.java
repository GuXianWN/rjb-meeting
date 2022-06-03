package com.guxian.common.entity;


import com.guxian.common.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSession {
    private String userId;
    private String userName;
    private RoleType role;
    private String token;
    private String ip;
}
