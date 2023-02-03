package com.example;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MonitorApplication {


//    SpringBoot  监控程序

    public static void main(String[] args) {


        SpringApplication.run(MonitorApplication.class);
    }
}


@FunctionalInterface   //拒绝策略
interface RejectPolicy{
   void reject();
}