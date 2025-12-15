package com.example.demo.config;

import io.minio.MinioClient;
import lombok.Data;
import okhttp3.OkHttpClient;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.TimeUnit;

@Configuration
@ConfigurationProperties(prefix = "minio")
@Data
public class MinioConfig {

    private String endpoint;
    private String accessKey;
    private String secretKey;
    private String bucketName;
    private int connectTimeout = 30; // seconds
    private int writeTimeout = 60;   // seconds
    private int readTimeout = 60;    // seconds

    @Bean
    public MinioClient minioClient() {
        OkHttpClient httpClient = new OkHttpClient.Builder()
                .connectTimeout(connectTimeout, TimeUnit.SECONDS)
                .writeTimeout(writeTimeout, TimeUnit.SECONDS)
                .readTimeout(readTimeout, TimeUnit.SECONDS)
                // Enforce HTTP/1.1 to avoid HTTP/2 negotiation issues in some Docker/Localhost setups
                .protocols(java.util.Collections.singletonList(okhttp3.Protocol.HTTP_1_1))
                .addInterceptor(chain -> chain.proceed(chain.request().newBuilder().removeHeader("Expect").build()))
                .build();

        String cleanedEndpoint = endpoint.endsWith("/") ? endpoint.substring(0, endpoint.length() - 1) : endpoint;

        return MinioClient.builder()
                .endpoint(cleanedEndpoint)
                .credentials(accessKey, secretKey)
                .httpClient(httpClient)
                .build();
    }
}