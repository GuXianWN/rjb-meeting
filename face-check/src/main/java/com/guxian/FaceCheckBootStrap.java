package com.guxian;

import com.guxian.facecheck.entity.UserFace;
import com.guxian.facecheck.repo.UserFaceRepo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

import java.time.Instant;
import java.util.Date;


@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan({"com.guxian.facecheck.entity"})
public class FaceCheckBootStrap {
    public static void main(String[] args) {
        var repo=SpringApplication.run(FaceCheckBootStrap.class, args).getBean(UserFaceRepo.class);
        System.out.println(repo.save(new UserFace()
                .setUserId(1L)
                .setFaceUrl("test")
                .setCreateTime(Date.from(Instant.now()))));
    }
}
