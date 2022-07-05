package com.guxian.auth.controller;

import com.guxian.auth.entity.User;
import com.guxian.auth.entity.vo.RegisterVo;
import com.guxian.auth.service.UserService;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/auth")
@Setter(onMethod_ = @Autowired)
public class RegisterController {
    private final UserService userService;

    public RegisterController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseData responseData(@RequestBody RegisterVo user) {

        return ResponseData.success(userService.register(user).orElseThrow(() -> new ServiceException(BizCodeEnum.UNKNOWN_EXCEPTION)));

    }
}
