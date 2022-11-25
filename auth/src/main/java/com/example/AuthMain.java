package com.example;


///用于表示启动类

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class AuthMain {
    public static void main(String[] args) {

        SpringApplication.run(AuthMain.class,args);

//        System.out.println("Hello world!");
    }
}