package com.guxian.auth.entity.dto;

import com.guxian.auth.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserDTO {
    private Long id;
    private String username;
    private String account;
    private String email;
    private String portraitUrl;
    private String mobile;

    public static UserDTO form(User user) {
        return new UserDTO(user.getId(), user.getUsername(), user.getAccount(), user.getEmail(), user.getPortraitUrl(), user.getMobile());
    }
}
