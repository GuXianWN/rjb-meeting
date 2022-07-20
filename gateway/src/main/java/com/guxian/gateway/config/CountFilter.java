package com.guxian.gateway.config;

import com.guxian.gateway.clients.CountClient;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.annotation.Resource;


@Component
public class CountFilter implements GlobalFilter, Ordered {
    @Resource
    private CountClient countClient;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        new Thread(()->{
            countClient.count();
        }).start();

        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return -1;
    }
}
