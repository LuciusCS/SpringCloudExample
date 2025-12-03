


## 为什么 Sentinel 配置在服务调用者中，而不是在服务被调用者中？



有Sentinel的情况
A[用户请求] --> B(LoadBalancer)
B -->|选择实例| C[Service A]
C -->|调用依赖| D{Sentinel监控}
D -->|正常| E[Service B]
D -->|熔断| F[降级逻辑]

## Sentinel 配置示例 (application.yml)
```yaml
spring:
  cloud:
    sentinel:
      transport:
        dashboard: localhost:8080 # Sentinel 控制台地址
      datasource:
        ds1:
          nacos:
            server-addr: localhost:8848
            dataId: ${spring.application.name}-flow-rules
            groupId: SENTINEL_GROUP
            rule-type: flow
```

## Sentinel vs Resilience4j
| 特性 | Sentinel | Resilience4j |
| :--- | :--- | :--- |
| **定位** | 流量防卫兵，关注流量控制、熔断降级、系统负载保护 | 容错库，关注熔断、重试、限流 |
| **生态** | Spring Cloud Alibaba 核心组件，与 Nacos/Dubbo 集成极佳 | Spring Cloud Circuit Breaker 官方推荐实现 |
| **控制台** | 提供开箱即用的控制台，支持动态规则推送 | 需要配合 Micrometer + Grafana 监控，无原生规则管理控制台 |
| **规则管理** | 支持动态数据源（Nacos, Apollo, Zookeeper） | 配置在本地文件或 Config Server |
| **适用场景** | 复杂的微服务架构，需要强大的流控和动态调整能力 | 标准 Spring Cloud 体系，偏向代码配置 |