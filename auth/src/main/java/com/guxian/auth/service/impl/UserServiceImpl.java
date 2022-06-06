package com.guxian.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.auth.entity.User;
import com.guxian.auth.entity.UserSession;
import com.guxian.auth.entity.vo.LoginVo;
import com.guxian.auth.service.UserService;
import com.guxian.auth.mapper.UserMapper;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Autowired
    private JwtUtils jwtUtils;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${guxian.jwt.expire}")
    private Long expire;


    @Override
    public Optional<User> findByUsernameLimitOne(String account) {
        return Optional.ofNullable(this.getOne(new QueryWrapper<User>().eq("account", account)));
    }

    @Override
    public String login(LoginVo loginVo, HttpServletRequest request) {
        User user = baseMapper.selectOne(new QueryWrapper<User>()
                .eq("account", loginVo.getUsername())
                .or().eq("email", loginVo.getUsername()));
        //是否存在
        if (user == null) {
            throw new ServiceException(BizCodeEnum.USER_NOT_EXIST);
        }
        //校验密码
        if (!passwordEncoder.matches(loginVo.getPassword(), user.getPassword())) {
            throw new ServiceException(BizCodeEnum.LOGIN_ACCOUNT_PASSWORD_EXCEPTION);
        }
        //生成token
        String token = jwtUtils.generateToken(user.getId());
        //用户放入redis 并且设置过期时间
        ValueOperations<String, String> opsForValue = redisTemplate.opsForValue();
        UserSession userSession = UserSession.forUser(user, request, token);
        opsForValue.set("user:" + user.getId(), JSONObject.toJSONString(userSession), expire, TimeUnit.SECONDS);
        return token;
    }
}




