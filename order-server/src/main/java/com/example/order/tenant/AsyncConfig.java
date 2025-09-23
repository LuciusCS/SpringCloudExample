package com.example.order.tenant;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * 用于创建异步操作 通过注解 @Async
 * // 启用异步支持
 */
@Configuration
@EnableAsync
public class AsyncConfig {


    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        // 配置核心线程池大小
        executor.setCorePoolSize(10);
        // 配置最大线程池大小
        executor.setMaxPoolSize(50);
        // 配置队列大小
        executor.setQueueCapacity(100);
        // 配置线程池前缀
        executor.setThreadNamePrefix("Async-");
        // 配置线程池拒绝策略
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());  // 线程池饱和时，直接在主线程中执行任务
        executor.initialize();

        return executor;
    }
}
