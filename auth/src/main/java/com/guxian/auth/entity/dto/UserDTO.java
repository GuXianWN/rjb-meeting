package com.guxian.auth.entity.dto;

import com.guxian.auth.entity.User;
import com.guxian.common.valid.UpdateGroup;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.junit.jupiter.params.shadow.com.univocity.parsers.annotations.Validate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.*;

@Data
@Accessors(chain = true)
public class UserDTO {
    @NotNull(message = "id 不能为空",groups = UpdateGroup.class)
    private Long id;

    private String username;

    @Null(message = "账号 无法修改！",groups = UpdateGroup.class)
    private String account;

    @Email(message = "email 格式错误",groups = UpdateGroup.class)
    private String email;

    private String portraitUrl;

    private String mobile;

    private String faceUrl;

    public static UserDTO form(User user) {
        return new UserDTO()
                .setId(user.getId())
                .setAccount(user.getAccount())
                .setMobile(user.getMobile())
                .setEmail(user.getEmail())
                .setUsername(user.getUsername())
                .setPortraitUrl(user.getPortraitUrl());
    }

    public static User toUser(UserDTO userDTO) {
        return new User()
                .setAccount(userDTO.getAccount())
                .setId(userDTO.getId())
                .setEmail(userDTO.getEmail())
                .setPortraitUrl(userDTO.getPortraitUrl())
                .setMobile(userDTO.getMobile())
                .setUsername(userDTO.getUsername());
    }
}
