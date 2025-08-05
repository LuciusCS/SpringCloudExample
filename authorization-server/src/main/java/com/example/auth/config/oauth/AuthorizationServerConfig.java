package com.example.auth.config.oauth;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;


/**
 * AuthorizationServerConfig 负责配置 OAuth2 授权服务器的端点和相关设置，如令牌端点 URL、令牌存储等。
 * 具体的作用以及，怎样体现出来
 *
 */

@Configuration
public class AuthorizationServerConfig {


    // 配置授权服务器的基础端点（如 /oauth2/token）
    @Bean
    public AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder()
                .issuer("http://localhost:8003")  /// 它定义了 Gateway（或资源服务器）如何解析和校验 JWT Token 的请求路径。
                .tokenEndpoint("/oauth2/token") // 自定义令牌端点URL
                .authorizationEndpoint("/oauth2/authorize")  // 授权端点（如果需要授权码模式）
//                .jwkSetEndpoint("/.well-known/jwks.json")  // JWK 公钥集端点
                .oidcUserInfoEndpoint("/userinfo")
                .build();
    }
}


