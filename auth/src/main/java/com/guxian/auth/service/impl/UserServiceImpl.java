package com.guxian.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.auth.entity.User;
import com.guxian.auth.service.UserService;
import com.guxian.auth.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{
    @Override
    public Optional<User> findByUsernameLimitOne(String account) {
        return Optional.ofNullable(this.getOne(new QueryWrapper<User>().eq("account",account)));
    }
}




