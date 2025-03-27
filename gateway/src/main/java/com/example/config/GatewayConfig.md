


## Sentinel 在 Spring Cloud Gateway 中的作用
### 流量控制
路由级限流：基于 routeId 对特定路由的请求进行限流，例如限制 /product/** 路径的 QPS27。
API 分组限流：自定义 API 分组（如 /order/** 和 /user/**），针对不同业务场景设置精细化限流策略27。
参数级限流：根据请求的 IP、Header、URL 参数等属性限流。例如限制同一 IP 的访问频率，或针对特定 Header 值的请求进行控制57。

### 熔断降级
当后端服务响应超时或异常率升高时，自动触发熔断，快速失败请求，避免级联故障78。

### 动态规则管理
支持通过 Nacos 配置中心动态更新限流规则，无需重启网关服务57。

### 系统自适应保护
根据网关的负载（如 CPU 使用率、请求 RT）动态调整流量，防止系统崩溃8。

### 服务监控与统计
实时统计每个服务的调用情况，提供健康度监控。

### 依赖库
```
        spring-cloud-starter-alibaba-sentinel  是 Sentinel 的 Spring Cloud 集成库。
        sentinel-transport-simple-http         是用于与 Sentinel 控制台交互的 HTTP 传输库
        spring-cloud-alibaba-sentinel-gateway  为 Spring Cloud Gateway 提供了流量控制、熔断降级、流量监控等功能

```
