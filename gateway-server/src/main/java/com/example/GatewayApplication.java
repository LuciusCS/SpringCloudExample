package com.example;

import com.alibaba.csp.sentinel.config.SentinelConfig;
import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class GatewayApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context= SpringApplication.run(GatewayApplication.class,args);

//        System.out.println(context.getEnvironment().getPropertSources());
//        System.out.println(context.getEnvironment().getPr);
    }
//    @PostConstruct
//    public void initSentinel() {
//        // 强制设置应用类型为 Gateway
//        System.setProperty(SentinelConfig.APP_TYPE_PROP_KEY, String.valueOf(SentinelGatewayConstants.GATEWAY_APP_TYPE));
//    }
}

