package com.guxian.auth.controller;

import com.guxian.auth.entity.User;
import com.guxian.auth.entity.dto.UserDTO;
import com.guxian.auth.service.UserService;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.SomeUtils;
import com.guxian.common.valid.UpdateGroup;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequestMapping("/user") //auth
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/count")
    public ResponseData count(){
        long count = userService.count();
        return ResponseData.success().data(count);
    }

    @GetMapping("/infor/{id}")
    @Cacheable(value = {"userInfor"}, key = "#id", sync = true)
    public ResponseData infor(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return ResponseData.success("查询成功")
                .data(UserDTO.form(user));
    }

    @PatchMapping
    public ResponseData updateUser(@Validated(UpdateGroup.class) @Valid @RequestBody UserDTO userDTO) {
        userService.modifyUserById(UserDTO.toUser(userDTO));
        return ResponseData.success();
    }

    /**
     *
     * @return 获取当前用户的个人信息
     */
    @GetMapping
    public ResponseData whoAmI() {
        var byId =
                Optional.ofNullable(
                userService.getById(CurrentUserSession.getUserSession().getUserId())).orElseThrow(
                        () -> new ServiceException(BizCodeEnum.NOT_LOGGED_IN));
        return ResponseData.success().data(UserDTO.form(byId));
    }

    @GetMapping("/username/{username}")
    public ResponseData information(@PathVariable String username) {
        var user = userService.getByUsername(username)
                .orElseThrow(() -> new ServiceException(BizCodeEnum.USER_NOT_EXIST));
        return ResponseData.success()
                .data(UserDTO.form(user));
    }

    @GetMapping("/{page}/{size}")
    public ResponseData getUserList(@PathVariable Integer page, @PathVariable Integer size) {
        return ResponseData.success()
                .data(userService.getUserList(page, size));
    }

    @SneakyThrows
    @PostMapping(value = "/upload")
    public ResponseData upload(@RequestParam(value = "file") MultipartFile file) {
        return userService.uploadPortrait(file
                , SomeUtils.buildPortrait(CurrentUserSession.getUserSession().getUserId()));
    }
}
