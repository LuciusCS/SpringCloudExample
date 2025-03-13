package com.example.auth.config;


import com.example.auth.oauth2.client.repository.OAuth2ClientRepository;
import com.example.auth.util.OAuth2AuthorizationDeserializer;
import com.example.auth.util.OAuth2AuthorizationSerializer;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
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
    @Autowired
    private  OAuth2ClientRepository clientRepository;

    @Bean
    public RedisTemplate<String, OAuth2Authorization> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, OAuth2Authorization> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        // 注册自定义反序列化器
        objectMapper.registerModule(new SimpleModule()
                .addDeserializer(OAuth2Authorization.class, new OAuth2AuthorizationDeserializer()));
        // 注册自定义序列化器和反序列化器
//        objectMapper.registerModule(new SimpleModule()
//                .addSerializer(OAuth2Authorization.class, new OAuth2AuthorizationSerializer())
//                .addDeserializer(OAuth2Authorization.class, new OAuth2AuthorizationDeserializer()));

        // 启用默认类型信息，添加@class字段
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY);

        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer(objectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }

}
