package com.guxian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FaceCheckBootStrap {
    public static void main(String[] args) {
         SpringApplication.run(FaceCheckBootStrap.class, args).getBeanFactory();
    }
}
