package com.guxian.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

@Slf4j
public class LocalTimeTest {
    @Test
    public void test(){
        LocalDateTime now = LocalDateTime.now();
        log.info("{}",now);
        log.info("{}",now.minusHours(1));
    }
}
