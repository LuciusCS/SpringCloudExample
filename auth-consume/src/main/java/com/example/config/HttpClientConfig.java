package com.example.config;


import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class HttpClientConfig {
    @Bean
    public RequestConfig requestConfig() {
        return RequestConfig.custom()
                .setConnectTimeout(30000)
                .setSocketTimeout(30000)
                .setConnectionRequestTimeout(30000)
                .build();
    }

    @Bean
    public CloseableHttpClient closeableHttpClient(RequestConfig requestConfig) {
        return HttpClients.custom()
                .setDefaultRequestConfig(requestConfig)
                .build();
    }
}
