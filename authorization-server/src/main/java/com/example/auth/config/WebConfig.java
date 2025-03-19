package com.example.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring Boot默认的ObjectMapper不包含activateDefaultTyping配置，因此无需额外配置即可处理普通JSON请求。
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public MappingJackson2HttpMessageConverter jacksonMessageConverter() {
        // 使用Spring Boot默认的ObjectMapper，避免与Redis配置冲突
        return new MappingJackson2HttpMessageConverter();
    }
}