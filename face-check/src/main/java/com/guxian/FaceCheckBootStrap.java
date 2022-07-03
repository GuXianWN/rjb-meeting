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
        var dllPath = SomeUtils.getResourceAsStream("lib/opencv/opencv_java455.dll");
        var altXmlPath = SomeUtils.getResourceAsStream("haarcascade_frontalface_alt.xml");
        var fileCacheUtils = new FileCacheUtils("/face-check-core");
        var static_dll = fileCacheUtils.saveFile(dllPath,"opencv_java455.dll");
        var static_altXml = fileCacheUtils.saveFile(altXmlPath,"haarcascade_frontalface_alt.xml");
        log.info("{}================", dllPath);
        System.load(static_dll.getAbsolutePath());
        return new CascadeClassifier(static_altXml.getAbsolutePath());
    }


}
