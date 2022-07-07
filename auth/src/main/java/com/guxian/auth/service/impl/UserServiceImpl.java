package com.guxian.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.guxian.auth.entity.User;
import com.guxian.auth.entity.UserSession;
import com.guxian.auth.entity.UserStatus;
import com.guxian.auth.entity.vo.LoginVo;
import com.guxian.auth.entity.vo.RegisterVo;
import com.guxian.auth.service.UserService;
import com.guxian.auth.mapper.UserMapper;
import com.guxian.auth.entity.RoleType;
import com.guxian.common.entity.PageData;
import com.guxian.common.entity.ResponseData;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.openfegin.facecheck.FaceCheckController;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.FileCacheUtils;
import com.guxian.common.utils.JwtUtils;
import com.guxian.common.utils.SomeUtils;
import org.json.JSONString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
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
    private PasswordEncoder passwordEncoder;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    @Value("${guxian.jwt.expire}")
    private Long expire;

    @Autowired
    private FaceCheckController faceCheckController;


    @Override
    public Optional<User> findByAccountLimitOne(String account) {
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
        UserSession userSession = UserSession.forUser(user, request, token);
        ValueOperations<String, String> ops = redisTemplate.opsForValue();
        ops.set("user:" + user.getId(), JSON.toJSONString(userSession), expire, TimeUnit.SECONDS);
        return token;
    }

    @Override
    public void logout(HttpServletRequest request) {
        Long uid = jwtUtils.getUid(request);
        //todo: qwq 可以加匹配token再删除
        redisTemplate.delete("user:" + uid);
    }


    //todo : add email to check user.
    @Override
    public Optional<User> register(RegisterVo user) {
        var byOne = this.getOne(new QueryWrapper<User>()
                .eq("username", user.getUsername()));
        if (byOne != null) {
            throw new ServiceException(BizCodeEnum.USER_EXIST_EXCEPTION);
        }
        User user1 = new User()
                .setUsername(user.getUsername())
                .setPassword(passwordEncoder.encode(user.getPassword()))
                .setAccount(user.getAccount())
                .setCreateTime(Date.from(Instant.now()))
                .setStatus(UserStatus.NORMAL)
                .setRoleId(RoleType.ROLE_USER);
        this.save(user1);
        return Optional.of(user1);
    }

    @Override
    public Optional<User> getByUsername(String username) {
        return Optional.ofNullable(this.getOne(new QueryWrapper<User>()
                .eq("username", username)));
    }

    @Override
    public void modifyUserById(User user) {
        var byId = this.getById(user.getId());
        if (byId == null || // Id don't exist or update return false
                !this.updateById(user)) {
            throw new ServiceException(BizCodeEnum.USER_NOT_EXIST);
        }
    }

    @Override
    public PageData getUserList(Long page, Long size) {
        var userPage = new Page<User>(page, size);
        var list = baseMapper.selectPage(userPage, null);

        return new PageData(page, size, list.getTotal(), list.getRecords());
    }

    @Override
    public ResponseData uploadPortrait(MultipartFile file, String buildPortrait) {
        var json = faceCheckController.uploadFile(file, SomeUtils.buildPortrait(CurrentUserSession.getUserSession().getUserId()));
        if (!json.startsWith("http")) {
            return ResponseData.parser(json);
        }
        return ResponseData.success().data(json);
    }
}




