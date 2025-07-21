//package com.example.authorization.client.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.oauth2.client.*;
//import org.springframework.security.oauth2.client.registration.ClientRegistration;
//import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
//import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
//import org.springframework.security.oauth2.client.web.AuthenticatedPrincipalOAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.DefaultOAuth2AuthorizedClientManager;
//import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository;
//import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
//import org.springframework.security.oauth2.core.AuthorizationGrantType;
//import org.springframework.web.reactive.function.client.WebClient;
//
//@Configuration
//public class OAuth2ClientConfig {
//    // 显式创建 ClientRegistration (解决自动配置失败问题)
//    @Bean
//    public ClientRegistration clientRegistration() {
//        return ClientRegistration.withRegistrationId("oidc-client")
//                .clientId("client1234")
//                .clientSecret("secret")
//                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .tokenUri("http://localhost:8003/oauth2/token")
//                .scope("read", "write")
//                .build();
//    }
//
//    // 显式创建 ClientRegistrationRepository
//    @Bean
//    public ClientRegistrationRepository clientRegistrationRepository(
//            ClientRegistration clientRegistration) {
//        return new InMemoryClientRegistrationRepository(clientRegistration);
//    }
//
//    // 配置授权客户端管理器
//    @Bean
//    public OAuth2AuthorizedClientManager authorizedClientManager(
//            ClientRegistrationRepository clientRegistrationRepository,
//            OAuth2AuthorizedClientRepository authorizedClientRepository) {
//
//        OAuth2AuthorizedClientProvider authorizedClientProvider =
//                OAuth2AuthorizedClientProviderBuilder.builder()
//                        .clientCredentials()
//                        .build();
//
//        DefaultOAuth2AuthorizedClientManager authorizedClientManager =
//                new DefaultOAuth2AuthorizedClientManager(
//                        clientRegistrationRepository,
//                        authorizedClientRepository);
//
//        authorizedClientManager.setAuthorizedClientProvider(authorizedClientProvider);
//
//        return authorizedClientManager;
//    }
//
//    // 配置 WebClient
//    @Bean
//    public WebClient webClient(OAuth2AuthorizedClientManager authorizedClientManager) {
//        ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2Client =
//                new ServletOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager);
//
//        oauth2Client.setDefaultClientRegistrationId("oidc-client");
//
//        return WebClient.builder()
//                .apply(oauth2Client.oauth2Configuration())
//                .build();
//    }
//
//    @Bean
//    public OAuth2AuthorizedClientRepository authorizedClientRepository(
//            OAuth2AuthorizedClientService authorizedClientService) {
//
//        return new AuthenticatedPrincipalOAuth2AuthorizedClientRepository(authorizedClientService);
//    }
//}