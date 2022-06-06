package com.guxian.common.permissions;

import com.alibaba.fastjson2.JSON;
import com.guxian.common.RoleType;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.JwtUtils;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log4j2
@Component
@NoArgsConstructor
public class PermissionsCheck implements HandlerInterceptor {

    static final String USER_PREFIX = "user:";
    static final String ROLE_PREFIX = "role:";
    private JwtUtils jwtUtils;
    private RedisTemplate<String, String> redisTemplate;
    AntPathMatcher antPathMatcher = new AntPathMatcher();


    @Autowired
    public PermissionsCheck(JwtUtils jwtUtils, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtils = jwtUtils;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        var ops = redisTemplate.opsForValue();
        var currentRole =1;
        var requestURI = request.getRequestURI();
        if (jwtUtils.hasToken(request)) { // 如果有token
            var uid = jwtUtils.getUid(request);
            var user = JSON.parseObject(ops.get(USER_PREFIX + uid.toString()), UserSession.class);
            if (user == null) {
                log.error("{}未登录",uid);
                throw new ServiceException(BizCodeEnum.USER_NOT_EXIST);
            }
            currentRole = user.getRole();
        }
        List<String> accessUrls = JSON.parseArray(ops.get(ROLE_PREFIX + currentRole), String.class);
        if (accessUrls==null){
            log.error("{}未设置权限",currentRole);
            throw new ServiceException(BizCodeEnum.NO_ACCESS);
        }
        if (accessUrls.stream().anyMatch(url -> antPathMatcher.match(url, requestURI))) {
            return true;
        }
        throw new ServiceException(BizCodeEnum.NO_ACCESS);
    }
}
