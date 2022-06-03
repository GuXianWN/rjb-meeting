package com.guxian;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Main {
    public static void main(String[] args) {
        var d=SpringApplication.run(Main.class, args).getBeanFactory();
    }
}
