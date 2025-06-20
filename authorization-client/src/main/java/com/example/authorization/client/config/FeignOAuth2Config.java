package com.example.authorization.client.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;

@Configuration
public class FeignOAuth2Config  implements RequestInterceptor {

    @Autowired
    private OAuth2AuthorizedClientService authorizedClientService;

    @Override
    public void apply(RequestTemplate template) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        OAuth2AuthorizedClient client = authorizedClientService.loadAuthorizedClient(
                "client1234",
                auth.getName()
        );
        template.header("Authorization", "Bearer " + client.getAccessToken().getTokenValue());
    }
}
