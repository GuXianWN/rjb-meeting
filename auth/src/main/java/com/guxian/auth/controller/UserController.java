package com.guxian.auth.controller;

import com.guxian.auth.entity.User;
import com.guxian.auth.entity.dto.UserDTO;
import com.guxian.auth.service.UserService;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import org.assertj.core.util.Lists;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Optional;

@RestController
@RequestMapping("/user") //auth
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/infor/{id}")
    @Cacheable(value = {"userInfor"}, key = "#id", sync = true)
    public ResponseData infor(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return ResponseData.success("查询成功")
                .data(UserDTO.form(user));
    }


    @GetMapping
    public ResponseData whoAmI() {
        var byId = Optional.ofNullable(userService.getById(CurrentUserSession.getUserSession().getUserId()))
                .orElseThrow(() -> new ServiceException(BizCodeEnum.NOT_LOGGED_IN));
        return ResponseData.success().data(UserDTO.form(byId));
    }

    @GetMapping
    public ResponseData information(@RequestBody String username) {
        return ResponseData.success()
                .data(userService.getByUsername(username)
                        .orElseThrow(() -> new ServiceException(BizCodeEnum.USER_NOT_EXIST)));
    }

}
