//package com.example.config;
//
//
//import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RateLimitConfig {
//
//    @Bean
//    public RedisRateLimiter redisRateLimiter() {
//        return new RedisRateLimiter(5, 10);  // 每秒新增5个令牌，最大桶容量10
//    }
//}
