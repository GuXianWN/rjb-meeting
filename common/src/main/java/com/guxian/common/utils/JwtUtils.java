package com.guxian.common.utils;

import com.alibaba.fastjson.JSON;
import com.guxian.common.entity.UserSession;
import com.guxian.common.exception.BizCodeEnum;
import com.guxian.common.exception.ServiceException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;



@Component
@Data
@Slf4j
public class JwtUtils {
    @Value("${guxian.jwt.secret}")
    private String secret;
    @Value("${guxian.jwt.expire}")
    private long expire;
    @Autowired
    private RedisTemplate<String,String> redisTemplate;


    /**
     * 生成jwt token
     */
    public String generateToken(Long userId) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);

        Date nowDate = new Date();
        //过期时间
        Date expireDate = new Date(nowDate.getTime() + expire * 1000);
        return Jwts.builder()
                .setHeaderParam("type", "JWT")
                .setClaims(claims)
                .setExpiration(expireDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public Optional<Claims> getClaimByToken(String token) {
        try {
            return Optional.of(Jwts.parser()
                    .setSigningKey(secret)
                    .parseClaimsJws(token)
                    .getBody());
        } catch (Exception e) {
            log.error("解析token失败：{}", e.getMessage());
            return Optional.empty();
        }
    }

    public Long getUid(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        if (!StringUtils.hasText(token) || !token.startsWith("Bearer ") || token.split(" ").length != 2) {
            throw new ServiceException(BizCodeEnum.GET_OAUTH_TOKEN_EXCEPTION);
        }
        String body = token.split(" ")[1];
        return getClaimByToken(body)
                .orElseThrow(() -> new ServiceException(BizCodeEnum.GET_OAUTH_TOKEN_EXCEPTION))
                .get("userId", Long.class);
    }

    public boolean hasToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        return StringUtils.hasText(token) && token.startsWith("Bearer ");
    }

    public UserSession getUserForRedis(Long uid){
        var ops = redisTemplate.opsForValue();
        UserSession user = JSON.parseObject(ops.get("user:" + uid), UserSession.class);
        if (user==null){
            throw new ServiceException(BizCodeEnum.NOT_LOGGED_IN);
        }
        return user;
    }

    /**
     * token是否过期
     *
     * @return true：过期
     */
    public boolean isTokenExpired(Date expiration) {
        return expiration.before(new Date());
    }

//    public boolean RoleVerifyAndException(Long uid,HttpServletRequest request, RoleType roleType){
//        Long uid1 = getUid(request);
//        if (uid==uid1){
//            return true;
//        }
//        UserSession user = getUserForRedis(uid1);
//        if (user.getRole()>=roleType.getExplain()){
//            return true;
//        }
//        throw new ServiceException(BizCodeEnum.NO_ACCESS);
//    }
}
