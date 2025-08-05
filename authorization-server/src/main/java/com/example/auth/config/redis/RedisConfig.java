package com.example.auth.config.redis;


import com.example.auth.bean.User;
import com.example.auth.oauth2.client.repository.OAuth2ClientRepository;
import com.example.auth.util.*;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;

@Configuration
public class RedisConfig {

    @Autowired
    private RegisteredClientRepository registeredClientRepository; // 注入客户端仓库

    @Autowired
    private  OAuth2ClientRepository clientRepository;

    @Bean
    public RedisTemplate<String, OAuth2Authorization> redisTemplate(
            RedisConnectionFactory connectionFactory,
            @Qualifier("redisObjectMapper")  ObjectMapper securityObjectMapper) { // 使用自定义配置的ObjectMapper

        RedisTemplate<String, OAuth2Authorization> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);

        // 使用增强后的ObjectMapper
        GenericJackson2JsonRedisSerializer serializer =
                new GenericJackson2JsonRedisSerializer(securityObjectMapper);

        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }



    /// 在Redis配置中创建专用的ObjectMapper，并仅将其用于Redis序列化，避免影响Spring MVC的默认配置。
    /// ObjectMapper 被注册为一个 @Bean，并且通过 @Qualifier("redisObjectMapper") 进行限定，只在 Redis 相关的 RedisTemplate 里使用。
    /// 不会影响 Spring MVC 默认的 JSON 处理，即不会影响 @RequestBody 解析 JSON 请求的行为。
    ///
    ///
    ///
    /// redisTemplate 方法的 ObjectMapper 参数是由 Spring 自动注入的，这里的 securityObjectMapper
    /// 其实是 Spring MVC 统一的 ObjectMapper，可能导致 Redis 自定义的 ObjectMapper 影响全局 JSON 解析。
    ///
    @Bean(name = "redisObjectMapper")
    public ObjectMapper securityObjectMapper(RegisteredClientRepository clientRepository) {
        ObjectMapper objectMapper = new ObjectMapper();

        // 注册必要模块
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.registerModules(SecurityJackson2Modules.getModules(getClass().getClassLoader()));
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());

        // 注册自定义User类的Mix-in
        objectMapper.addMixIn(User.class, UserMixin.class);
        objectMapper.registerModule(new Jdk8Module()); // 支持 Optional
        objectMapper.registerModule(new JavaTimeModule()); // 支持 LocalDateTime
        // 注册UsernamePasswordAuthenticationToken的反序列化器
        SimpleModule securityModule = new SimpleModule();
        securityModule.addDeserializer(
                UsernamePasswordAuthenticationToken.class,
                new UsernamePasswordAuthenticationTokenDeserializer()
        );
        objectMapper.registerModule(securityModule);

        // 注册OAuth2Authorization的反序列化器（替换原有实现）
        objectMapper.registerModule(new SimpleModule()
                .addDeserializer(OAuth2Authorization.class, new OAuth2AuthorizationDeserializer(clientRepository))
        );

        // 启用默认类型信息（仅用于Redis）
        objectMapper.activateDefaultTyping(
                objectMapper.getPolymorphicTypeValidator(),
                ObjectMapper.DefaultTyping.NON_FINAL,
                JsonTypeInfo.As.PROPERTY
        );

        return objectMapper;
    }

}
