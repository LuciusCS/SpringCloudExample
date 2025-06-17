package com.example.filter;


import io.micrometer.common.util.StringUtils;
import io.netty.util.internal.StringUtil;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class AuthorizeFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ///在gateway这里需要有token才能继续进行请求
        String token = exchange.getRequest().getQueryParams().getFirst("token");
//        if (StringUtils.isBlank(token)) {
//
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//
//            return exchange.getResponse().setComplete();
//        }
        return chain.filter(exchange);
    }

    @Override
    public int getOrder() {
        return 0;
    }
}
