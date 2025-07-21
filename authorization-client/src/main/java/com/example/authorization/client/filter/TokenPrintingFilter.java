package com.example.authorization.client.filter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import reactor.core.publisher.Mono;

//令牌打印拦截器（HTTP 请求级别）
@Slf4j
@Component
public class TokenPrintingFilter implements ExchangeFilterFunction {

    @Override
    public Mono<ClientResponse> filter(ClientRequest request, ExchangeFunction next) {
        // 检查并打印 Authorization 头
        if (request.headers().containsKey(HttpHeaders.AUTHORIZATION)) {
            String authHeader = request.headers().getFirst(HttpHeaders.AUTHORIZATION);
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String token = authHeader.substring(7);
                log.debug("Request to {} with Bearer token: {}",
                        request.url(),
                        maskToken(token));
            }
        }

        // 继续请求执行
        return next.exchange(request);
    }

    private String maskToken(String token) {
        if (token == null || token.length() < 10) return "*****";
        return token.substring(0, 4) + "..." + token.substring(token.length() - 4);
    }
}
