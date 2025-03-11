package com.example.auth.config;


import com.example.auth.util.OAuth2AuthorizationDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, OAuth2Authorization> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, OAuth2Authorization> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 配置 ObjectMapper 以支持 Java 8 时间类型和自定义反序列化器
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule()); // 支持 Java 8 时间类型
        objectMapper.registerModule(new SimpleModule().addDeserializer(
                OAuth2Authorization.class, new OAuth2AuthorizationDeserializer()));

        // 使用 GenericJackson2JsonRedisSerializer 进行序列化和反序列化
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        // 设置 Key 和 Value 的序列化器
        template.setKeySerializer(new StringRedisSerializer()); // Key 使用字符串序列化器
        template.setValueSerializer(serializer); // Value 使用 JSON 序列化器
        template.setHashKeySerializer(new StringRedisSerializer()); // Hash Key 使用字符串序列化器
        template.setHashValueSerializer(serializer); // Hash Value 使用 JSON 序列化器

        // 初始化 RedisTemplate
        template.afterPropertiesSet();
        return template;
    }

}
