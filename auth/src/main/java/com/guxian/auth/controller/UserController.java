package com.guxian.auth.controller;

import com.guxian.auth.entity.User;
import com.guxian.auth.entity.dto.UserDTO;
import com.guxian.auth.service.UserService;
import com.guxian.common.entity.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
public class UserController {
    @Autowired
    private UserService userService;

    @GetMapping("/infor/{id}")
    @Cacheable(value = {"userInfor"}, key = "#id",sync = true)
    public ResponseData infor(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return ResponseData.success("查询成功")
                .data(UserDTO.form(user));
    }
}
