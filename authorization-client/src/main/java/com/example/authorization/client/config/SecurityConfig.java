package com.example.authorization.client.config;

import com.example.authorization.client.filter.TokenPrintingFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.client.*;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 */
@Configuration
public class SecurityConfig {

    /**
     * 配置 OAuth2 客户端的安全过滤器链。
     * 该方法允许所有请求通过，并启用了 OAuth2 客户端功能。
     *
     * @param http HttpSecurity 配置对象
     * @return SecurityFilterChain 安全过滤器链
     * @throws Exception 可能的异常
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorizeRequests ->
                        //Easy to test, open permissions
                        authorizeRequests.anyRequest().permitAll()
                )
                .oauth2Client(withDefaults());
        return http.build();
    }

    /**
     * 配置 WebClient 以支持 OAuth2 授权。
     *
     * @param authorizedClientManager OAuth2 授权客户端管理器
     * @return 配置了 OAuth2 过滤器的 WebClient 实例
     */
    @Bean
    WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager, TokenPrintingFilter tokenInterceptor) {
        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client = new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);

        /// // ✅ 设置默认 clientRegistrationId , 所以在 ResourceService 中就不需要配置
        ///     .attributes(clientRegistrationId("oidc-client")) 了，如果有多个 clientRegistrationId 需要另外的配置
        // 配置令牌刷新策略

        /// 如果有多个 clientRegistrationId , 删除下面两行即可，不需要做其他的修改
        oauth2Client.setDefaultClientRegistrationId("oidc-client");
        oauth2Client.setDefaultOAuth2AuthorizedClient(true);

        /// .filter(oauth2Client) 注册了 OAuth2 的 ExchangeFilterFunction，实现了请求时自动携带和管理 token 的能力。
        return WebClient.builder()
                .filter(oauth2Client)
                .filter(tokenInterceptor)
                .build();
    }

    /**
     * 配置 OAuth2 授权客户端管理器。
     * 适用于基于客户端凭据的 OAuth2 授权。
     *
     * @param clientRegistrationRepository 客户端注册信息存储库
     * @param authorizedClientService      授权客户端服务
     * @return OAuth2AuthorizedClientManager 实例
     */
    @Bean
    OAuth2AuthorizedClientManager authorizedClientManager(ClientRegistrationRepository clientRegistrationRepository,
                                                          OAuth2AuthorizedClientService authorizedClientService) {

        ///authorizedClientProvider 它会自动重新获取 token 并替换旧 token
        OAuth2AuthorizedClientProvider authorizedClientProvider = OAuth2AuthorizedClientProviderBuilder
                .builder()
                .clientCredentials()
                .build();
        AuthorizedClientServiceOAuth2AuthorizedClientManager authorizedClientManager = new AuthorizedClientServiceOAuth2AuthorizedClientManager(clientRegistrationRepository, authorizedClientService);
        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);

        return authorizedClientManager;
    }
}
