package com.example.authorization.resource;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;


@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class Oauth2AuthorizationResourceApplication {

//    新版本的Springboot 在出错后，不再输出错误到控制台了，而是将异常在Application main方法中抛出。
    public static void main(String[] args) {


        try {
            SpringApplication.run(Oauth2AuthorizationResourceApplication.class, args);

        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
