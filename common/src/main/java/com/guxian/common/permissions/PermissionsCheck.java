package com.guxian.common.permissions;

import com.alibaba.fastjson2.JSON;
import com.guxian.common.enums.RoleType;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import com.guxian.common.utils.CurrentUserSession;
import com.guxian.common.utils.JwtUtils;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

@Log4j2
@Component
@NoArgsConstructor
public class PermissionsCheck implements HandlerInterceptor ,WebMvcConfigurer{

    static final String USER_PREFIX = "user:";
    static final String ROLE_PREFIX = "role:";
    private JwtUtils jwtUtils;
    private RedisTemplate<String, String> redisTemplate;
    AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Value("${close-token-check}")
    private boolean closed = false;

    @Autowired
    public PermissionsCheck(JwtUtils jwtUtils,
                            RedisTemplate<String, String> redisTemplate) {
        this.jwtUtils = jwtUtils;
        this.redisTemplate = redisTemplate;
    }


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (closed) {
            log.warn("token check closed");
            return true;
        }
        var ops = redisTemplate.opsForValue();
        var currentRole = RoleType.ROLE_GUEST.getExplain();
        var requestURI = request.getRequestURI();
        var uid = 0L;
        var user = new UserSession();
        if (jwtUtils.hasToken(request)) { // 如果有token 有权限
            uid = jwtUtils.getUid(request);
            user = JSON.parseObject(ops.get(USER_PREFIX + uid), UserSession.class);
            if (user == null) {
                log.error("user is not UserSession");
                throw new ServiceException(BizCodeEnum.NOT_LOGGED_IN);
            }
            currentRole = user.getRole();
        }
        List<String> accessUrls = JSON.parseArray(ops.get(ROLE_PREFIX + currentRole.toString()), String.class);

//        assert accessUrls != null;

        CurrentUserSession.setUserSession(user,closed);

        return true;
//        // 放行白名单
//        if (accessUrls.stream().anyMatch(url -> antPathMatcher.match(url, requestURI))) {
//            return true;
//        }
//
//        throw new ServiceException(BizCodeEnum.NO_ACCESS);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(this);
    }
}

