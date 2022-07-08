package com.guxian;

import com.guxian.common.utils.FileCacheUtils;
import com.guxian.common.utils.SomeUtils;
import lombok.extern.log4j.Log4j2;
import org.aspectj.lang.annotation.Aspect;
import org.opencv.core.Core;
import org.opencv.objdetect.CascadeClassifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import java.io.File;

@SpringBootApplication(scanBasePackages = "com.guxian.*")
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
        nu.pattern.OpenCV.loadShared();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String xml= SomeUtils.getPath()+"static/haarcascade_frontalface_alt.xml";
        log.info("xml=========>{}",xml);
        File xmlFile = new File(xml);
        log.info("xml can read=========>{}",xmlFile.canRead());
        log.info("xml can write=========>{}",xmlFile.canWrite());
        return new CascadeClassifier(xml.toString());
    }
}
