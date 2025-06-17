


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

## Sentinel Dashboard 在gateway中的特殊配置


## 直接请求 gateway中的接口，Sentinel Dashboard 中可能不会有请求显示
需要通过 gateway  请求到微服务才可以


## Sentinel需要添加流控规则/熔断规则


## 版本问题
  spring-cloud-starter-alibaba-sentinel和 spring-cloud-alibaba-sentinel-gateway 使用的是2023.0.3.2版本使用
  对应的 的gateway-dashboard是1.8.6 ，造成gateway-dashboard 将gateway当成普通微服务

需要在pom.xml文件中指定 1.8.8

```xml
        <dependency>
    <!--        该依赖 让 Sentinel 能够拦截 Gateway 的所有流量，否则 Sentinel 只能监控簇点链路，而无法细粒度监控 API。-->
    <groupId>com.alibaba.csp</groupId>
    <artifactId>sentinel-spring-cloud-gateway-adapter</artifactId>
    <version>1.8.8</version>
    </dependency>
      <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-core</artifactId>
            <version>1.8.8</version>
        </dependency>
        <dependency>
            <groupId>com.alibaba.csp</groupId>
            <artifactId>sentinel-datasource-nacos</artifactId>
            <version>1.8.8</version> <!-- 版本根据你的 Spring Boot 版本适配 -->
        </dependency>
```
  

## 网址  这一个用在Sentinels 是用来做什么
 
http://localhost:8719/tree?type=root


## 配置信息持久化

参考： https://blog.bigcoder.cn/article/303eac17.html