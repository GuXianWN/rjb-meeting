package com.guxian.auth.config;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Log4j2
@Configuration
public class InitAuth {

    @Value("${auth.jwt.secret}")
    private String secret;

    public String getSecret() {
        log.info("current secret is : {}", secret);
        return secret;
    }
}
