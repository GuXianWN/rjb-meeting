package com.guxian.common.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserSession {
    private Long userId;
    private String userName;
    private Integer role;
    private String token;
    private String ip;
}
