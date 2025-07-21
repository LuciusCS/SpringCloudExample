package com.example.authorization.client.service;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

@Service
public class ResourceService {

    private final WebClient webClient;

    public ResourceService(WebClient webClient) {
        this.webClient = webClient;
    }

    public String fetchProtectedResource() {
        return webClient.get()
                .uri("http://127.0.0.1:8003/helloTestController/hello") // 资源服务器地址
                .attributes(clientRegistrationId("oidc-client"))

                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}