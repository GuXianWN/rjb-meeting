package com.guxian.auth;

import com.guxian.common.utils.JwtUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest
class TokenTest {
    @Autowired
    JwtUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void should_return_token() {
        String token = jwtUtils.generateToken(1L);
        System.out.println(token);
    }

    @Test
    public void encode() {
        String rawPassword = "admin";
        System.out.println(passwordEncoder.encode(rawPassword));
    }

    @Test
    public void matches(){
        String rawPassword="123456";
        String encodedPassword = passwordEncoder.encode(rawPassword);
        System.out.println(passwordEncoder.matches(rawPassword,encodedPassword));
    }
}
