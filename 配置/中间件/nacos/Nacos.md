


🟩 1. Nacos 有没有负载均衡？
答案是：有，但是有限。

✅ Nacos 提供了“负载均衡策略的辅助信息”：

✅ 服务实例的权重
✅ 实例健康状态
✅ 注册中心提供可用 IP 列表
❌ 但 Nacos 本身不“路由请求”，它不是一个网关或代理。

🔍 你看到的“权重配置”、“优先级”等功能，其实是 为了帮助客户端更好地进行负载均衡选择 —— 但最终谁去选？由客户端代码（Feign + LoadBalancer）决定。


❗Nacos 不具备真正的负载均衡能力，它只是负载均衡“信息源”，而不是“负载策略执行者”。
✅ 真正选择调用哪个实例的是 Spring Cloud LoadBalancer；
✅ NacosLoadBalancer 是它的实现，负责用 Nacos 的权重等信息来“选一个”。


## Spring Cloud LoadBalancer VS  NacosLoadBalancer

Spring Cloud LoadBalancer 是 Spring Cloud 提供的一个 负载均衡器接口，它为不同的负载均衡策略提供了一个统一的抽象层；
NacosLoadBalancer 是 Nacos 实现的一个负载均衡策略，它是基于 Spring Cloud LoadBalancer 的接口，
**使用 Nacos 提供的服务实例列表（包括健康、权重等）**来决定选择哪个服务实例。

Spring Cloud 中配置了 Nacos 后，Feign 会自动使用 NacosLoadBalancer 来选择服务实例，因为 Spring Cloud 默认会把 Nacos 的服务实例列表作为 LoadBalancer 的数据源。
具体而言，NacosLoadBalancer 会接管服务选择过程，使用 Nacos 提供的服务实例列表来确定访问哪个具体实例。



## Nacos 负载均衡 工作流程：
1、服务注册：当服务通过 Nacos 注册时，Nacos 维护了可用服务的实例列表。
2、Feign 调用：在 Feign 中，调用目标服务时，会使用 Spring Cloud LoadBalancer 来进行负载均衡。
3、NacosLoadBalancer：Spring Cloud LoadBalancer 会使用 NacosLoadBalancer 来从 Nacos 获取服务实例列表，然后根据负载均衡策略（如轮询、加权等）选择一个实例。
4、请求转发：选择好服务实例后，Feign 会将请求发向该实例。


配置

```
spring:
  cloud:
    nacos:
      discovery:
        server-addr: localhost:8848  # Nacos 注册中心地址
    loadbalancer:
      ribbon:
        enabled: false  # Ribbon 被废弃，使用 LoadBalancer
```

## Nacos 2.x 与 gRPC
Nacos 2.0 引入了 gRPC 协议，相比 1.x 的 HTTP 短连接，性能有了显著提升：
1.  **长连接**：减少了连接建立和销毁的开销。
2.  **实时推送**：配置变更和由于服务上下线引起的服务列表变更，可以更实时地推送到客户端。
3.  **双向通信**：支持更高效的客户端与服务端交互。

注意：Nacos 2.x 会占用两个端口，主端口（默认 8848）和 gRPC 端口（主端口 + 1000，即 9848）。确保防火墙开放了这两个端口。
## Nacos 可以将主机名，自动创建group，在开发环境中使用
## 将Nacos的配置ip写入到环境变量中