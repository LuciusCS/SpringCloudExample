package com.example.auth.services;

import org.springframework.security.oauth2.server.authorization.OAuth2Authorization;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
//import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.stereotype.Service;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

@Service
public class RedisOAuth2AuthorizationService implements OAuth2AuthorizationService {
    private static final String PREFIX = "oauth2:authorization:";
    private final RedisTemplate<String, OAuth2Authorization> redisTemplate;

    public RedisOAuth2AuthorizationService(RedisTemplate<String, OAuth2Authorization> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }
    @Transactional
    @Override
    public void save(OAuth2Authorization authorization) {
        String authKey = PREFIX + authorization.getId();
        redisTemplate.opsForValue().set(authKey, authorization);

        // 存储 accessToken -> authorizationId 的映射
        if (authorization.getAccessToken() != null) {
            String accessToken = authorization.getAccessToken().getToken().getTokenValue();
            redisTemplate.opsForHash().put(PREFIX + "tokens", accessToken, authorization.getId());
        }

        // 存储 refreshToken -> authorizationId 的映射
        if (authorization.getRefreshToken() != null) {
            String refreshToken = authorization.getRefreshToken().getToken().getTokenValue();
            redisTemplate.opsForHash().put(PREFIX + "tokens", refreshToken, authorization.getId());

        }
    }

    @Override
    public void remove(OAuth2Authorization authorization) {
        redisTemplate.delete(PREFIX + authorization.getId());
    }

    @Override
    public OAuth2Authorization findById(String id) {
        return redisTemplate.opsForValue().get(PREFIX + id);
    }

    @Override
    public OAuth2Authorization findByToken(String token, OAuth2TokenType tokenType) {
        // 通过 token 找到授权 ID
        String authorizationId = (String) redisTemplate.opsForHash().get(PREFIX + "tokens", token);
        if (authorizationId == null) {
            return null;
        }
        OAuth2Authorization authorization = redisTemplate.opsForValue().get(PREFIX + authorizationId);
        return authorization;
    }

}