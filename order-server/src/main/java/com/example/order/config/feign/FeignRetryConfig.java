//package com.example.order.config.feign;
//import feign.Retryer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class FeignRetryConfig {
//
//    @Bean
//    public Retryer feignRetryer() {
//        // 间隔100ms开始重试，最大间隔1s，最多尝试3次
//        return new Retryer.Default(100, 1000, 3);
//    }
//}
