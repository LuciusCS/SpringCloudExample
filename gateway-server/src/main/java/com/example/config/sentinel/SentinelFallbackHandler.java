package com.example.config.sentinel;
import com.alibaba.csp.sentinel.adapter.gateway.sc.callback.BlockRequestHandler;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityException;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeException;
import com.alibaba.csp.sentinel.slots.block.flow.FlowException;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowException;
import com.alibaba.csp.sentinel.slots.system.SystemBlockException;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.HashMap;
import java.util.Map;

@Component
public class SentinelFallbackHandler implements BlockRequestHandler {


    @Override
    public Mono<ServerResponse> handleRequest(ServerWebExchange serverWebExchange, Throwable throwable) {
        Map<String, Object> result = new HashMap<>();
        result.put("code", 429);
        result.put("message", "系统繁忙，请稍后再试");

        if (throwable instanceof FlowException) {
            result.put("message", "接口被限流了");
        } else if (throwable instanceof DegradeException) {
            result.put("message", "服务被降级了");
        } else if (throwable instanceof ParamFlowException) {
            result.put("message", "热点参数限流了");
        } else if (throwable instanceof SystemBlockException) {
            result.put("message", "触发系统保护规则");
        } else if (throwable instanceof AuthorityException) {
            result.put("message", "授权规则不通过");
        }

        return ServerResponse.status(429)
                .contentType(MediaType.APPLICATION_JSON)
                .body(BodyInserters.fromValue(result));
    }
}