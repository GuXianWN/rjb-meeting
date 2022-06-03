package com.guxian.common.permissions;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson2.JSON;
import com.guxian.common.RoleType;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.lang.NonNullApi;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.util.Assert;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

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
    public boolean preHandle(HttpServletRequest request,  HttpServletResponse response, Object handler) throws Exception {
        var ops = redisTemplate.opsForValue();
        var currentRole = RoleType.ROLE_GUEST;
        var requestURI = request.getRequestURI();
        if (jwtUtils.hasToken(request)) { // 如果有token 有权限
            var uid = jwtUtils.getUid(request);
            var user = JSON.parseObject(ops.get(USER_PREFIX + uid.toString()), UserSession.class);

            if (user == null) {
                log.error("user is not UserSession");
                throw new ServiceException(BizCodeEnum.USER_NOT_EXIST);
            }

            currentRole = user.getRole();
        }
        List<String> accessUrls = JSON.parseArray(ops.get(ROLE_PREFIX + currentRole.toString()), String.class);

        assert accessUrls != null;

        if (accessUrls.stream().anyMatch(url -> antPathMatcher.match(url, requestURI))) {
            return true;
        }
        throw new ServiceException(BizCodeEnum.NO_ACCESS);
    }
}
