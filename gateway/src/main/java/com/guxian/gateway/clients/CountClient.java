package com.guxian.gateway.clients;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("auth")
public interface CountClient {
    @PostMapping("/count")
    void count();
}
