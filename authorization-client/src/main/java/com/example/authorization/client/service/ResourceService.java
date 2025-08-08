package com.example.authorization.client.service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;
@Slf4j
@Service
public class ResourceService {

    private final WebClient webClient;
    private final OAuth2AuthorizedClientService authorizedClientService;
    public ResourceService(WebClient webClient, OAuth2AuthorizedClientService authorizedClientService) {
        this.webClient = webClient;
        this.authorizedClientService = authorizedClientService;
    }

    public String fetchProtectedResource() {

        // 获取当前授权客户端
        OAuth2AuthorizedClient authorizedClient =
                authorizedClientService.loadAuthorizedClient("oidc-client", "system");

        // 输出当前的 access_token
        if (authorizedClient != null) {
            String accessToken = authorizedClient.getAccessToken().getTokenValue();
            log.info("Current Access Token: " + accessToken);
        } else {
            log.info("No authorized client found for the specified client registration ID.");
        }

        /// // 在SecurityConfig的 webClient 设置默认 clientRegistrationId , 所以在 ResourceService 中就不需要配置
        ///     .attributes(clientRegistrationId("oidc-client")) 了，如果有多个 clientRegistrationId 需要另外的配置
        return webClient.get()
                .uri("http://127.0.0.1:8003/helloTestController/hello") // 资源服务器地址
                .attributes(clientRegistrationId("oidc-client"))

                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}