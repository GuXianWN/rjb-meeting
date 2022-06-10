package com.guxian.auth.service;

import com.guxian.auth.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.auth.entity.vo.LoginVo;
import com.guxian.auth.entity.vo.RegisterVo;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 *
 */
public interface UserService extends IService<User> {

    Optional<User> findByUsernameLimitOne(String username);

    String login(LoginVo loginVo, HttpServletRequest request);

    void logout(HttpServletRequest request);

    Optional<User> register(RegisterVo user);
}
