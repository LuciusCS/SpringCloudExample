package com.example.auth.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;


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
                .issuer("http://localhost:8003")
                .tokenEndpoint("/oauth2/token") // 自定义令牌端点URL
                .authorizationEndpoint("/oauth2/authorize")  // 授权端点（如果需要授权码模式）

                .oidcUserInfoEndpoint("/userinfo")
                .build();
    }
}


