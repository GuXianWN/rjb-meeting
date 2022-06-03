package com.guxian.common.permissions;

import com.guxian.common.utils.JwtUtils;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Setter(onMethod_ = @Autowired)
public class Test {
    private JwtUtils jwtUtils;
    private RedisTemplate<String, Object> redisTemplate;

    public void test() {
        System.out.println(1);
    }

}
