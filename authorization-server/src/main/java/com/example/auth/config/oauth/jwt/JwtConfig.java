package com.example.auth.config.oauth.jwt;


import cn.hutool.core.util.StrUtil;
import com.example.auth.bean.User;

import com.example.auth.constant.RedisConstants;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.security.KeyPair;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

import java.util.UUID;
import java.util.stream.Collectors;

/**
 * JWT 令牌配置
 */
@Configuration
public class JwtConfig {

    @Autowired
    private  StringRedisTemplate redisTemplate1;

    @Autowired
    private  KeyPair keyPair;
//
//    @Bean
//    public JwtEncoder jwtEncoder(KeyPair keyPair) {
//        JWK jwk = new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()) // 使用 RSA 公钥
//                .privateKey((RSAPrivateKey) keyPair.getPrivate()) // 绑定 RSA 私钥
//                .algorithm(JWSAlgorithm.RS256) // 指定算法 RS256
//                .build();
//        JWKSource<SecurityContext> jwkSource = (jwkSelector, context) -> new JWKSet(jwk).getKeys();
//        return new NimbusJwtEncoder(jwkSource);
//    }

    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    // 自定义令牌内容（添加用户角色）
    // 使用自定义字段
    @Bean
    public OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer() {
        return context -> {
            // 设置过期时间为 1 小时（60 * 60 秒）
            context.getClaims().claim("exp", System.currentTimeMillis() / 1000 + 60 * 60*7*24);

            /// 用于通过用户名密码模式登录
            if (context.getAuthorizationGrantType().getValue().equals("password")) {
                User user = (User) context.getPrincipal().getPrincipal();
                context.getClaims().claim("roles",
                        user.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority)
                                .collect(Collectors.toList())
                );
            }
            /// 用于客户端模式登录
            else if(context.getAuthorizationGrantType().getValue().equals("client_credential")){

                context.getClaims().claim("roles", "test");
            }else{
                context.getClaims().claim("roles","test");
            }
        };
    }


    /**
     * JWK（JWT密钥对）源
     *
     *
     */
    @Bean // <5>
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {

        // 尝试从Redis中获取JWKSet(JWT密钥对，包含非对称加密的公钥和私钥)
        String jwkSetStr = redisTemplate1.opsForValue().get(RedisConstants.JWK_SET_KEY);
        if (StrUtil.isNotBlank(jwkSetStr)) {
            // 如果存在，解析JWKSet并返回
            JWKSet jwkSet = JWKSet.parse(jwkSetStr);
            return new ImmutableJWKSet<>(jwkSet);
        } else {
            // 如果Redis中不存在JWKSet，生成新的JWKSet
//            KeyPair keyPair = generateRsaKey();
            RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
            RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

            // 构建RSAKey
            RSAKey rsaKey = new RSAKey.Builder(publicKey)
                    .privateKey(privateKey)
                    .keyID(UUID.randomUUID().toString())
                    .build();

            // 构建JWKSet
            JWKSet jwkSet = new JWKSet(rsaKey);

            // 将JWKSet存储在Redis中
            redisTemplate1.opsForValue().set(RedisConstants.JWK_SET_KEY, jwkSet.toString(Boolean.FALSE));
            return new ImmutableJWKSet<>(jwkSet);
        }

    }

}


