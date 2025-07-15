//package com.example.config.sentinel;
//
//import com.alibaba.csp.sentinel.adapter.gateway.common.SentinelGatewayConstants;
//import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiDefinition;
//import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPathPredicateItem;
//import com.alibaba.csp.sentinel.adapter.gateway.common.api.ApiPredicateItem;
//import com.alibaba.csp.sentinel.adapter.gateway.common.api.GatewayApiDefinitionManager;
//import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayFlowRule;
//import com.alibaba.csp.sentinel.adapter.gateway.common.rule.GatewayRuleManager;
//import com.alibaba.csp.sentinel.slots.block.RuleConstant;
//import jakarta.annotation.PostConstruct;
//import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import reactor.core.publisher.Mono;
//
//import java.util.HashSet;
//import java.util.Set;
//
///**
// * 由于 Gateway 不是普通的微服务，所以 Sentinel 不会自动识别 API，必须手动定义 API 资源。
// *
// *
// * 默认 Sentinel 只能监控簇点链路，但不会知道 /user/** 具体是什么 API。
// * 定义 API 资源后，Sentinel 可以细粒度统计流量，在 Sentinel Dashboard 中看到具体接口数据。
// */
//@Configuration
//public class GatewaySentinelConfig {
//    /**
//     * 重要
//     * 这个 KeyResolver 用于 在 Spring Cloud Gateway + Sentinel 组合中实现基于 IP 地址的流控。
//     * 它的作用是：
//     *
//     * 获取请求的客户端 IP 作为流控的 key（限流依据）。
//     * Mono.just() 使其成为一个 Reactive 异步流，适配 Gateway 的 Reactive 机制。
//     *
//     *
//     * 需要在yml文件中配置
//     *
//     * spring:
//     *   cloud:
//     *     sentinel:
//     *       transport:
//     *         dashboard: localhost:8080
//     *         port: 8719
//     *       gateway:
//     *         enabled: true  # 启用 Sentinel Gateway
//     * @return
//     */
//    @Bean
//    public KeyResolver userKeyResolver() {
//        return exchange -> Mono.just(exchange.getRequest().getRemoteAddress().getAddress().getHostAddress());
//    }
//
//    @PostConstruct
//    public void doInit() {
//        initGatewayRules();
//        initApiDefinitions();
//    }
//
//    /** 配置 Gateway 流控规则 */
//    private void initGatewayRules() {
//        Set<GatewayFlowRule> rules = new HashSet<>();
//        rules.add(new GatewayFlowRule("api-hello") // 绑定 API 资源
//                .setResourceMode(SentinelGatewayConstants.RESOURCE_MODE_CUSTOM_API_NAME)
//                .setGrade(RuleConstant.FLOW_GRADE_QPS)  // QPS 限流
//                .setCount(5) // 限制每秒最多 5 个请求
//                .setIntervalSec(60)); // 统计周期 60 秒
//
//        GatewayRuleManager.loadRules(rules);
//    }
//
//    /** 定义 API 资源 */
//    private void initApiDefinitions() {
//        Set<ApiDefinition> definitions = new HashSet<>();
//        ApiDefinition api1 = new ApiDefinition("api-hello") // 自定义 API 名称
//                .setPredicateItems(new HashSet<ApiPredicateItem>() {{
//                    add(new ApiPathPredicateItem().setPattern("/hello/**")); // 绑定路由
//                }});
//        definitions.add(api1);
//        GatewayApiDefinitionManager.loadApiDefinitions(definitions);
//    }
//}
