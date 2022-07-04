package com.guxian;

import com.guxian.common.utils.FileCacheUtils;
import com.guxian.common.utils.SomeUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;
import java.net.URL;

@SpringBootApplication
@EnableFeignClients
@EnableDiscoveryClient
@EntityScan({"com.guxian.facecheck.entity"})
@Log4j2
@ComponentScan("com.guxian.*")
public class FaceCheckBootStrap {
    public static void main(String[] args) {
        SpringApplication.run(FaceCheckBootStrap.class, args);
    }

    @Bean
    CascadeClassifier cascadeClassifier() {
        log.info("file demo========>");

        StringBuilder path= new StringBuilder(SomeUtils.getResource("application.yaml"));
        log.info("===========>{}",path);
        String[] split = path.toString().split("/");
        path = new StringBuilder();
        for (int i = 0; i < split.length-4; i++) {
            path.append(split[i]).append("/");
        }
        path.append("static/face-check-core");
        String dll=path.append("/opencv_java455.dll").toString();
        String xml=path.append("/haarcascade_frontalface_alt.xml").toString();
        dll="/"+dll;
        xml="/"+xml;
        File file = new File(dll);
        log.info("=========>{}",dll);
        log.info("can read=========>{}",file.canRead());
        log.info("can write=========>{}",file.canWrite());
//        System.load(dll);
        return new CascadeClassifier(xml.toString());
    }
}
