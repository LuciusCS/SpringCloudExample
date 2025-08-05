


需要结合Sentinel进行熔断以及限流处理



完整流程总结：
1、Sentinel 的 @SentinelResource 注解：
fallbackGetUser 方法用于处理单个 Feign 方法的熔断降级逻辑。当调用失败或超过设定的阈值时，Sentinel 会调用该方法。
2、 Feign 的 fallback 类：
SeckillFeignClientFallback 是 Feign 的统一降级机制，用于为整个 Feign 客户端提供回退逻辑。
无论哪个 Feign 方法调用失败，UserServiceFallback 都会执行降级操作。

## 为什么 Feign 的降级类需要？

全局降级处理：虽然 fallbackGetUser 可以在 Feign 方法熔断时返回备用值，但有时你可能需要为整个 Feign 客户端提供一个统一的降级策略。
SeckillFeignClientFallback 就是为整个 Feign 客户端提供降级的统一处理，它是 Feign 的回退机制的一部分，
确保在任何 Feign 调用失败时都能返回备用结果。
fallback 方法是方法级别的，而 UserServiceFallback 是类级别的降级。它们在不同的降级策略层次上起作用。


## 需要在 application.yml 文件中或者在Nacos中进行配置

```
spring:
  application:
    name: feign-sentinel-demo

  cloud:
    sentinel:
      transport:
        dashboard: http://localhost:8080  # Sentinel 控制台地址（可选）
      datasource:
        file:
          file: classpath:sentinel-rules.json  # 使用 JSON 文件来配置规则
      default:
        flow:
          rules:
            - resource: "getTestHello"
              count: 10  # 限流 QPS
              grade: 1  # 限流类型：1代表QPS，0代表线程数
              strategy: 0  # 阻塞策略
              controlBehavior: 0  # 丢弃策略
              limitApp: default  # 限流规则适用的应用名
```


## Sentinel 中的 resource 配置

在 Sentinel 中，resource 是一个抽象的“资源标识”，它表示你需要进行流量控制、熔断或限流保护的 方法、接口或服务。在实际使用中，resource 可以是：
接口方法（比如 getTestHello）；
服务名（如 user-service）；
特定的业务操作（比如 order-create）；


## 需要补充 sentinel-rules.json