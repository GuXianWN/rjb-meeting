package com.guxian.common.config;

import com.guxian.common.permissions.PermissionsCheck;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class InterceptorConfig implements WebMvcConfigurer {
    @Autowired
    private PermissionsCheck permissionsCheck;
    @Value("${close-token-check}")
    private Boolean closeTokenCheck;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 如果有多个拦截器，继续registry.add往下添加就可以啦
        if (!closeTokenCheck) {
            registry.addInterceptor(permissionsCheck).addPathPatterns("/**");
        }
    }
}
