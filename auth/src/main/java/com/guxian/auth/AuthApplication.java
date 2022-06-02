package com.guxian.auth;

import com.guxian.auth.entity.PermitAllUrlProperties;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@MapperScan("com.guxian.auth.mapper")
@SpringBootApplication
@EnableDiscoveryClient
@EnableConfigurationProperties({PermitAllUrlProperties.class})
public class AuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(AuthApplication.class, args);
	}

}
