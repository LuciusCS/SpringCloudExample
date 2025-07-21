//package com.example.authorization.client.config;
//
//import jakarta.annotation.PostConstruct;
//
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
//import org.springframework.stereotype.Service;
//
//
//@Service
//public class TokenPreloader {
//
//    private static final Logger logger = LoggerFactory.getLogger(TokenPreloader.class);
//    private final OAuth2AuthorizedClientManager authorizedClientManager;
//
//    public TokenPreloader(OAuth2AuthorizedClientManager authorizedClientManager) {
//        this.authorizedClientManager = authorizedClientManager;
//    }
//
//    @PostConstruct
//    public void preloadToken() {
//        try {
//            OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
//                    .withClientRegistrationId("oidc-client")
//                    .principal("system-service")
//                    .build();
//
//            OAuth2AuthorizedClient authorizedClient =
//                    authorizedClientManager.authorize(authorizeRequest);
//
//            if (authorizedClient != null && authorizedClient.getAccessToken() != null) {
//                logger.info("Successfully preloaded access token: {}",
//                        authorizedClient.getAccessToken().getTokenValue());
//            } else {
//                logger.error("Failed to preload access token: Authorization response is null");
//            }
//        } catch (Exception e) {
//            logger.error("Token preloading failed", e);
//            // 这里可以添加重试逻辑或通知机制
//        }
//    }
//}
