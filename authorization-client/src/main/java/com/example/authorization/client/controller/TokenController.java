package com.example.authorization.client.controller;

import org.springframework.security.oauth2.core.OAuth2AccessToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

import java.util.Map;

/**
 * 令牌端点信息
 */
@RestController
public class TokenController {
    private final OAuth2AuthorizedClientService clientService;

    public TokenController(OAuth2AuthorizedClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/token-info")
    public Map<String, Object> getTokenInfo() {
        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
                "oidc-client", "system");

        if (client == null) {
            return Map.of("status", "no_token");
        }

        OAuth2AccessToken token = client.getAccessToken();

        return Map.of(
                "token_type", token.getTokenType().getValue(),
                "token_value", token.getTokenValue(),
                "issued_at", token.getIssuedAt(),
                "expires_at", token.getExpiresAt(),
                "scopes", token.getScopes(),
                "client_id", client.getClientRegistration().getClientId()
        );
    }
}
