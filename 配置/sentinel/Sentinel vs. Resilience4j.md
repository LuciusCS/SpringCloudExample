

Sentinel 适用于 限流、网关流控、动态规则管理。
Resilience4j 适用于 熔断、降级、线程隔离、重试。Netflix Hystrix 替代者，提供 熔断、限流、重试、缓存，轻量级、基于 Java 8 函数式编程
🚀 如果你用 Spring Cloud Alibaba，推荐 Sentinel！
🚀 如果你用 Spring Cloud 官方组件，推荐 Resilience4j！


对比项             	限流（Rate Limiting）	            熔断（Circuit Breaker）
目的              	控制流量，防止系统超载	                故障隔离，防止雪崩
触发条件	               高并发请求超过阈值	               依赖服务异常，如超时或高错误率
策略                  	丢弃超出请求	                        切断流量，提供降级
典型算法            	令牌桶、漏桶、滑动窗口	            熔断器状态机（Closed → Open → Half-Open）
适用场景	            API 网关、限流策略、防爬虫	            依赖服务不可用、雪崩防护
实现方式	            Redis、Spring Cloud Gateway	        Sentinel、Resilience4j



如果接口高并发，先限流**，避免流量打爆服务**
如果接口访问的下游服务不稳定，启用熔断**，防止级联故障**
Spring Cloud Gateway 限流 + Sentinel 熔断，最佳实践

用户请求太多？限流！（控制访问频率）
下游服务故障？熔断！（防止故障扩散）
两者可以结合，提升系统稳定性 🚀


组件                      主要用途	                适用架构	                        特点
Redisson 限流	基于 Redis 实现的分布式限流	微服务 / 网关 / 单体	        轻量级、支持 Redis Cluster、适合高并发场景
Sentinel	    流量控制、熔断降级	            微服务、Spring Cloud	        阿里开源、支持分布式限流、内置控制台
Resilience4j	熔断、限流、重试、超时控制	Java 单体 / Spring Cloud 微服务	轻量级、本地限流、适合单体应用

## Resilience4j   vs Sentinel

Resilience4j + CircuitBreaker 和 Sentinel 在熔断（以及限流、降级等功能）方面确实是类似的

| 特性                    | **Resilience4j + CircuitBreaker**                          | **Sentinel**                                    |
| --------------------- |---------------------------------------------------------  ---| ----------------------------------------------- |
| **熔断功能**              | ✅ 提供熔断功能，基于失败率、请求数等条件自动断开服务              | ✅ 提供熔断功能，自动判断服务调用是否健康                           |
| **限流功能**              | ✅ 提供限流功能，可以设置基于 QPS 或并发线程数的流量控制           | ✅ 提供限流功能，支持 QPS、线程数等多维度控制                       |
| **降级功能**              | ✅ 支持服务降级，失败时返回默认值或其他逻辑                       | ✅ 支持服务降级，失败时返回默认值或通过规则处理                        |
| **动态配置支持**           | ❌ 需要额外配置和管理，例如通过代码配置熔断规则                   | ✅ 支持通过控制台或动态配置更新熔断规则                            |
| **策略灵活性**             | ✅ 提供详细的熔断规则和自定义配置，灵活性较高                     | ✅ 提供多种细粒度的规则控制，可以基于资源、接口、方法等多维度进行配置             |
| **集成复杂度**             | ✅ 需要开发者手动配置，灵活但相对较复杂                            ✅ 易于集成，尤其适合复杂分布式系统的保护，配置简便                      |
| **适用场景**              | 适用于需要灵活、可定制的熔断、限流、重试机制的系统                  | 适用于大规模分布式系统，尤其是需要细粒度控制的高并发场景                    |
| **与 Spring Cloud 集成**  | ✅ 提供 `spring-cloud-starter-circuitbreaker-resilience4j` 集成 | ✅ 提供 `spring-cloud-starter-alibaba-sentinel` 集成 |
| **熔断器的开关控制**         | 通过 `CircuitBreaker` 注解和策略进行控制                       | 通过 `@SentinelResource` 注解进行控制                   |
| **监控与可视化**            | ❌ 默认不支持监控和可视化，需要集成其他监控工具                     | ✅ 提供内建的控制台，支持服务调用的实时监控和动态规则调整                   |


1、Resilience4j 提供的是一个 独立的熔断器，在 Feign 调用时，你需要显式地启用 CircuitBreaker 来应用熔断逻辑。
如果你在 Feign 中使用 Resilience4j，你需要配置 CircuitBreaker，并通过 AOP 或注解来控制熔断规则。
Resilience4j 必须明确使用 CircuitBreaker 来控制熔断行为。
```
feign:
  circuitbreaker:
    enabled: true  # 启用 Resilience4j CircuitBreaker
```

```
@FeignClient(name = "user-service", fallback = UserServiceFallback.class)
public interface UserServiceClient {

    @CircuitBreaker(name = "userService", fallbackMethod = "fallbackGetUser")
    @GetMapping("/users/{id}")
    User getUser(@PathVariable("id") Long id);

    default User fallbackGetUser(Long id, Throwable t) {
        return new User(id, "Fallback User");
    }
}
```

2、 Sentinel 的工作机制与 Resilience4j 的不同，它在 服务保护层集成了 熔断、限流、降级 等多种功能。
通过 注解（例如 @SentinelResource），Sentinel 可以直接在 Feign 中启用，并通过配置文件或控制台来动态控制策略，
无需额外配置 CircuitBreaker。 使用 Sentinel 时，无需显式配置熔断器，只需要用 @SentinelResource 注解来指定熔断规则：

```
@FeignClient(name = "user-service", fallback = UserServiceFallback.class)
public interface UserServiceClient {

    @SentinelResource(value = "getUser", fallback = "fallbackGetUser")
    @GetMapping("/users/{id}")
    User getUser(@PathVariable("id") Long id);

    default User fallbackGetUser(Long id, Throwable t) {
        return new User(id, "Fallback User");
    }
}
```
Sentinel 的 @SentinelResource 注解：Sentinel 会自动为 Feign 方法提供熔断、降级等功能，而无需额外配置。
CircuitBreaker 不是必需的，因为 Sentinel 自带熔断机制。
