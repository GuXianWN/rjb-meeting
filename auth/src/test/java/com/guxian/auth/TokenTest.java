package com.guxian.auth;

import com.guxian.common.utils.JwtUtils;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class TokenTest {
    @Autowired
    private JwtUtils jwtUtils;
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

    @Test
    public void toToken(){
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", 1);

        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + 1);
        String token = Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, "secret")
                .compact();
        System.out.println(token);
    }

    @Test
    public void parse(){
        jwtUtils.getClaimByToken("eyJ0eXBlIjoiSldUIiwiYWxnIjoiSFM1MTIifQ.eyJleHAiOjE2NTU4NjUyMTEsInVzZXJJZCI6MX0.kCm2exOHsT2uKPIcofl_cGpotY5p3g_AyYtRbxxrS75ZVM0tASbtdNr2LSIul0DmrxGdd3PAHM-YUA2z0nX1CA");
    }
}
