package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@SpringBootApplication
public class AuthMainConsume {
    public static void main(String[] args) {

        SpringApplication.run(AuthMainConsume.class,args);

//        System.out.println("Hello world!");
    }
}