package com.guxian.auth.service;

import com.guxian.auth.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.guxian.auth.entity.vo.LoginVo;
import com.guxian.auth.entity.vo.RegisterVo;
import com.guxian.common.entity.PageData;
import com.guxian.common.entity.ResponseData;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 *
 */
public interface UserService extends IService<User> {

    Optional<User> findByAccountLimitOne(String username);

    String login(LoginVo loginVo, HttpServletRequest request);

    void logout(HttpServletRequest request);

    Optional<User> register(RegisterVo user);

    Optional<User> getByUsername(String username);

    void modifyUserById(User user);

    PageData getUserList(Long page, Long size);

    ResponseData uploadPortrait(MultipartFile file, String buildPortrait);
}
