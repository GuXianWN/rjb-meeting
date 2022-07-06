package com.guxian.common.redis;

import lombok.Data;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@Data
public class RedisUtils {

    public  static ValueOperations<String, Object> ops;
    private final RedisTemplate<String, Object> redisTemplate;

    public RedisUtils(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @PostConstruct
    public void init() {
        RedisUtils.ops=redisTemplate.opsForValue();
    }


}
