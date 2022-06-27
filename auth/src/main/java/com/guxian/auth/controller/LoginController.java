package com.guxian.auth.controller;

import com.guxian.auth.entity.vo.LoginVo;
import com.guxian.auth.service.UserService;
import com.guxian.common.entity.ResponseData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class LoginController {
    @Autowired
    private UserService userService;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @PostMapping("/login")
    public ResponseData login(@RequestBody LoginVo loginVo, HttpServletRequest request) {
        String token = userService.login(loginVo, request);
        return ResponseData.success("登录成功")
                .data("token", token);
    }

    @PostMapping("/logout")
    public ResponseData logout(HttpServletRequest request) {
        userService.logout(request);
        return ResponseData.success("退出成功");
    }


}
