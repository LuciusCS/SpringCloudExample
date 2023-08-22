package com.example;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context= SpringApplication.run(GatewayApplication.class);

//        System.out.println(context.getEnvironment().getPropertSources());
//        System.out.println(context.getEnvironment().getPr);
    }
}
