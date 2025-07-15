package com.example.order.config.feign;
import okhttp3.OkHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.TimeUnit;


@Configuration
public class OkHttpConfig {
    @Bean
    public OkHttpClient okHttpClient() {
        // 配置 OkHttpClient，启用长连接和连接池
        return new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)   // 设置连接超时
                .readTimeout(30, TimeUnit.SECONDS)      // 设置读取超时
                .writeTimeout(30, TimeUnit.SECONDS)     // 设置写入超时
                .connectionPool(new okhttp3.ConnectionPool(10, 5, TimeUnit.MINUTES))  // 配置连接池
                .build();
    }
//
//    @Bean
//    public OkHttpClient okHttpClient() {
//        return new OkHttpClient.Builder()
//                .connectTimeout(5, TimeUnit.SECONDS)
//                .readTimeout(10, TimeUnit.SECONDS)
//                .writeTimeout(10, TimeUnit.SECONDS)
//                .connectionPool(new ConnectionPool(200, 5, TimeUnit.MINUTES))
//                .build();
//    }
}
