package com.guxian.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.auth.entity.User;
import com.guxian.auth.service.UserService;
import com.guxian.auth.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author GuXian
* @description 针对表【user】的数据库操作Service实现
* @createDate 2022-05-31 21:08:07
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




