package com.guxian.common.feignapi.config;

import feign.Logger;
import org.springframework.context.annotation.Bean;


/**
 * @author GuXianWN
 * @date 2022/04/09 11:02
 **/
public class DefaultFeignConfiguration {
    @Bean
    public Logger.Level feignLogLevel() {
        // 日志级别为BASIC
        return Logger.Level.BASIC;
    }
}
