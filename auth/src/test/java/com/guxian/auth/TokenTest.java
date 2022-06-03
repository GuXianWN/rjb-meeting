package com.guxian.auth;

import com.guxian.common.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class TokenTest {
    @Autowired
    JwtUtils jwtUtils;
    @Test
    public void should_return_token() {
        String token = jwtUtils.generateToken(1L);
        System.out.println(token);
    }
}
