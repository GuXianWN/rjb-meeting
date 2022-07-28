package com.guxian.auth.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.guxian.auth.entity.User;
import com.guxian.auth.entity.dto.UserDTO;
import com.guxian.auth.service.UserService;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.entity.UserFace;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.openfegin.facecheck.FaceCheckClient;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.SomeUtils;
import com.guxian.common.valid.UpdateGroup;
import lombok.SneakyThrows;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user") //auth
@CacheConfig(cacheNames = "user_cache")
public class UserController {
    private final UserService userService;
    @Resource
    private FaceCheckClient faceCheckClient;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/count")
    public ResponseData count(){
        long count = userService.count();
        return ResponseData.success().data(count);
    }

    @GetMapping("/infor/{id}")
    @Cacheable(key = "#id", sync = true)
    public ResponseData infor(@PathVariable("id") Long id) {
        User user = userService.getById(id);
        return ResponseData.success("查询成功")
                .data(UserDTO.form(user));
    }

    @GetMapping("/infor/account/{account}")
    public ResponseData getByAccount(@PathVariable String account) {
        List<User> list = userService.getByAccount(account);
        List<UserDTO> list1 = list.stream().map(v -> {
            v.setPassword("");
            return UserDTO.form(v);
        }).collect(Collectors.toList());
        return ResponseData.success("查询成功")
                .data(list1);
    }

    @PatchMapping
    @CachePut(key = "#p0.id")
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
        ResponseData data = faceCheckClient.getFaces(CurrentUserSession.getUserSession().getUserId());
        String json = JSON.toJSONString(data.getData());
        UserFace userFace = JSONObject.parseObject(json, UserFace.class);
        return ResponseData.success().data(UserDTO.form(byId).setFaceUrl(userFace.getFaceUrl()));
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
