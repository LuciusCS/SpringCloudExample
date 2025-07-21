package com.example.authorization.client.service;

import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.OAuth2AuthorizeRequest;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
@Slf4j
@Service
public class TokenRefreshService {

    /// 用于进行数据输出
//    private static final Log log = LogFactory.getLog(TokenRefreshService.class);

    private static final String CLIENT_REGISTRATION_ID = "oidc-client";
    private static final String PRINCIPAL_NAME = "system";  ///"system" 是一个约定俗成的占位符名称
    private static final int REFRESH_THRESHOLD_MINUTES = 1;
    private static final int REFRESH_INTERVAL_MINUTES = 2;

    private final OAuth2AuthorizedClientManager authorizedClientManager;
    private final OAuth2AuthorizedClientService authorizedClientService;
    private final Authentication authentication;

    public TokenRefreshService(
            OAuth2AuthorizedClientManager authorizedClientManager,
            OAuth2AuthorizedClientService authorizedClientService) {

        this.authorizedClientManager = authorizedClientManager;
        this.authorizedClientService = authorizedClientService;
        this.authentication = createSystemAuthentication();
    }

    @Scheduled(fixedRate = REFRESH_INTERVAL_MINUTES * 60 * 1000)
    public void refreshTokenIfNeeded() {
        try {
            OAuth2AuthorizedClient currentClient = authorizedClientService
                    .loadAuthorizedClient(CLIENT_REGISTRATION_ID, PRINCIPAL_NAME);

            boolean needsRefresh = shouldRefreshToken(currentClient);

            if (needsRefresh) {
                log.info("Token is about to expire, refreshing...");
                OAuth2AuthorizedClient newClient = refreshAccessToken();

                if (newClient != null) {
                    logTokenDetails("New token details", newClient.getAccessToken());
                }
            } else if (currentClient != null) {
                logTokenDetails("Current token details", currentClient.getAccessToken());
            }
        } catch (Exception e) {
            System.err.println("Token refresh failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private OAuth2AuthorizedClient refreshAccessToken() {
        OAuth2AuthorizeRequest authorizeRequest = OAuth2AuthorizeRequest
                .withClientRegistrationId(CLIENT_REGISTRATION_ID)
                .principal(authentication)
                .build();

        OAuth2AuthorizedClient newClient = authorizedClientManager.authorize(authorizeRequest);

        if (newClient != null) {
            // 保存新令牌（覆盖旧令牌）
            authorizedClientService.saveAuthorizedClient(newClient, authentication);
            return newClient;
        }

        System.err.println("Failed to refresh token");
        return null;
    }

    private boolean shouldRefreshToken(OAuth2AuthorizedClient client) {
        if (client == null) {
            log.info("No existing token found, acquiring new token");
            return true;
        }

        OAuth2AccessToken token = client.getAccessToken();
        if (token == null || token.getExpiresAt() == null) {
            log.info("Invalid token found, acquiring new token");
            return true;
        }

        long minutesToExpire = Duration.between(
                Instant.now(), token.getExpiresAt()).toMinutes();

        log.info("Current token expires in: " + minutesToExpire + " minutes");

        return minutesToExpire <= REFRESH_THRESHOLD_MINUTES;
    }

    private void logTokenDetails(String message, OAuth2AccessToken token) {
        if (token == null) return;

        long minutesToExpire = Duration.between(
                Instant.now(), token.getExpiresAt()).toMinutes();

        log.info(message);
        log.info("  Token value: " + token.getTokenValue());
        log.info("  maskToken value: " + maskToken(token.getTokenValue()));
        log.info("  Expires at: " + token.getExpiresAt());
        log.info("  Minutes until expiration: " + minutesToExpire);
        log.info("-----------------------------------------");
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 10) return "****";
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }

    private Authentication createSystemAuthentication() {
        return new UsernamePasswordAuthenticationToken(
                PRINCIPAL_NAME,
                null,
                AuthorityUtils.createAuthorityList("ROLE_SYSTEM"));
    }
}