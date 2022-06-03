package com.guxian.common.permissions;

import com.guxian.common.entity.UserSession;
import com.guxian.common.utils.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.log4j.Log4j2;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Log4j2
@Component
@AllArgsConstructor
@Setter(onMethod_ = @Autowired)
@NoArgsConstructor
public class PermissionsCheck implements HandlerInterceptor {

    static final String USER_PREFIX = "user:";
    private JwtUtils jwtUtils;
    private RedisTemplate<String, Object> redisTemplate;


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        new Test().test();
        String role = "";
        UserSession userSession = null;
        if (!jwtUtils.hasToken(request)) { // 如果没有token 是游客 没有权限
            role = "游客";
        } else {
            Long uid = jwtUtils.getUid(request);
            var user = redisTemplate.opsForValue().get(USER_PREFIX + uid.toString());
            if (userSession instanceof UserSession) {
                userSession = (UserSession) user;
            }
        }


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Configuration
    class InterceptorConfig implements WebMvcConfigurer {

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            //&emsp;如果有多个拦截器，继续registry.add往下添加就可以啦
            registry.addInterceptor(new PermissionsCheck()).addPathPatterns("/**");
        }
    }
}
