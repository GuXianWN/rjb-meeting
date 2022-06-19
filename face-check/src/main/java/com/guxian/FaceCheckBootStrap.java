package com.guxian;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableDiscoveryClient
@MapperScan("com.guxian.faceheck.mapper")
@SpringBootApplication(scanBasePackages = "com.guxian.*")
public class FaceCheckBootStrap {
    public static void main(String[] args) {
         SpringApplication.run(FaceCheckBootStrap.class, args);
    }
}
