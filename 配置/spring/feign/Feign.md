


## Feign Client 客户端实现选择
1. 生产环境不建议使用 Client.Default   ApacheHttpClient
2. 高并发生产环境推荐使用OkHttpClient
3. 支持负载均衡： OkHttpClient + LoadBalancer（支持同时使用），
   OkHttpClient 作为 Feign 的底层 HTTP 客户端，负责处理 HTTP 请求和响应。
   LoadBalancerClient 负责提供负载均衡的功能，决定每次请求应该发送到哪个服务实例。
4. 在生产环境下Feign必须与Ribbon结合在一起使用，所以方法处理器MethodHandler的客户端client成员，必须要是具备负载均衡能力的
   LoadBalancerFeignClient 类型，而不是完成HTTP 请求提交的ApacheHttpClient 等类型。只有在负载均衡计算出最佳的Provider
   实例之后，才能开始HTTP提交
5. Ribbon 已经停止维护，推荐使用 LoadBalanceClient


## 微服务之间使用Feign调用，采用Http长连接

不需要显式禁用 Ribbon，因为在 Spring Cloud 2020 及以后的版本中，Spring Cloud LoadBalancer 已默认取代了 Ribbon。
只需配置 OkHttpClient 来支持长连接，并将其与 Feign 配合使用。

### OpenFeign组件

     组件	                                  用途	                                 是否自动创建                          谁调用谁
feign.Client	               Feign 的底层 HTTP 请求执行器接口（真正发请求的）   	✅ 是                              Feign 框架调用它
feign.Client.Default	       默认实现，基于 HttpURLConnection	                ✅ 是（若没有自定义）
feign.okhttp.OkHttpClient	   基于 OkHttp 的 Feign 客户端实现	                    ✅ 是（若引入 OkHttp 且配置）       被 LoadBalancerFeignClient 包裹
LoadBalancerFeignClient	       一个装饰器，它包裹了底层 Feign client                ✅ 自动装配                        被 Spring 注入到 Feign
                              （如 OkHttpClient）来加上服务发现和负载均衡功能	
OkHttpClient（来自 okhttp3 包） 	真正执行 HTTP 请求的底层客户端（非 feign 的）	    ✅ 是（如果使用                    被 feign.okhttp.OkHttpClient 调用
                                                                                   feign.okhttp.OkHttpClient）

#### 结构图解：
```
           @FeignClient
               ↓
        Feign 自动生成代理
               ↓
    通过 feign.Client 执行 HTTP 请求
               ↓
LoadBalancerFeignClient（装饰器，增强客户端支持服务发现）
               ↓
   feign.okhttp.OkHttpClient（如果配置了 OkHttp）
               ↓        
   okhttp3.OkHttpClient（真正发起 HTTP 请求）
```

## Spring Cloud LoadBalancer VS  NacosLoadBalancer

Spring Cloud LoadBalancer 是 Spring Cloud 提供的一个 负载均衡器接口，它为不同的负载均衡策略提供了一个统一的抽象层；
NacosLoadBalancer 是 Nacos 实现的一个负载均衡策略，它是基于 Spring Cloud LoadBalancer 的接口，
  **使用 Nacos 提供的服务实例列表（包括健康、权重等）**来决定选择哪个服务实例。

Spring Cloud 中配置了 Nacos 后，Feign 会自动使用 NacosLoadBalancer 来选择服务实例，因为 Spring Cloud 默认会把 Nacos 的服务实例列表作为 LoadBalancer 的数据源。
具体而言，NacosLoadBalancer 会接管服务选择过程，使用 Nacos 提供的服务实例列表来确定访问哪个具体实例。


## Spring Cloud 中的“分层结构”


                            ┌──────────────────────────────┐
                            │      应用层（业务逻辑）         │
                            └────────────┬─────────────────┘
                                         │
                         熔断/限流保护层 │   ←─── Sentinel / Hystrix / Resilience4j
                                         ▼
                            ┌──────────────────────────────┐
                            │ FeignClient / RestTemplate    │
                            └────────────┬─────────────────┘
                                         │
                   负载均衡/服务调用选择 │   ←─── Ribbon / LoadBalancer / Nacos LoadBalancer
                                         ▼
                            ┌──────────────────────────────┐
                            │ 服务发现层（如 Nacos、Eureka） │
                            └──────────────────────────────┘





层级	                        功能职责	                                        框架对比                            	状态
🔐 熔断/限流保护层	服务间调用过程中发生错误时自动降级/快速失败，防止系统雪崩	- Hystrix（弃用）
                                                                        - ✅ Sentinel                          同一层功能，可互替
                                                                        - ✅ Resilience4j	    

  🔁 负载均衡层	      从多个服务实例中选择一个 IP:PORT 发起请求	            - Ribbon（弃用）
                                                                        - ✅ Spring Cloud LoadBalancer         同一层功能，可互替
                                                                        - ✅ NacosLoadBalancer	

  📡 服务发现层	     提供服务列表与健康实例（非执行层，仅数据源）          	- Eureka、Nacos、Consul 等	           无替代，各自实现


                 ┌──────────────────────┐
                 │   Controller 层调用   │
                 └────────┬─────────────┘
                          ↓
                 ┌──────────────────────┐
                 │ Sentinel（是否被限流）│ ←───── 保护逻辑
                 └────────┬─────────────┘
                          ↓
                ┌──────────────────────┐
                │ FeignClient 动态代理 │
                └────────┬─────────────┘
                          ↓
                ┌──────────────────────┐
                │ LoadBalancer 选服务实例│ ←───── 选 IP:PORT
                └────────┬─────────────┘
                          ↓
                ┌──────────────────────┐
                │ OkHttpClient 发请求  │
                └──────────────────────┘



## FeignClient中的fallback 使用Sentinel
```

```

## Sentinel 可以通过 spring-cloud-starter-alibaba-sentinel 自动接管 Feign；

如果你使用的是 Spring Cloud Alibaba：
默认就已经集成 Sentinel Feign 熔断器；
你不需要再使用 spring-cloud-starter-circuitbreaker-resilience4j；
配置如下即可生效：

```
feign:
  sentinel:
     enabled: true
```

## FeignClient 中的 fallback

fallback 是 Feign 提供的一个机制，用于 在请求失败时返回预设的替代结果。它通常用于：
熔断：当服务不可用时，可以通过 fallback 来提供“降级”逻辑，避免调用失败导致整个系统崩溃。
降级：比如，当远程服务不可达时，fallback 可以返回本地缓存数据或预设的结果。
‼️ 关联：fallback 只是提供了 降级处理，它并没有实现 熔断。熔断机制（如 CircuitBreaker）
‼️ 是 更加智能的，它会判断什么时候开启熔断、何时恢复，而不仅仅是简单的返回“备用”数据。

```java
@FeignClient(name = "user-service", fallback = UserClientFallback.class)
public interface UserClient {
    @GetMapping("/api/user/{id}")
    User getUser(@PathVariable("id") Long id);
}

@Component
public class UserClientFallback implements UserClient {
    public User getUser(Long id) {
        return new User(id, "Fallback User");
    }
}
//UserClientFallback 会出现不执行的情况，
//Feign 的 retry 配置问题。Feign 的超时异常通常会触发重试机制，而 fallback 方法只有在请求完全失败时才会被调用，即 Feign 没有重试时才会触发 fallback。
```
🌟 这时候，Feign 会通过 Sentinel 实现熔断、降级。

如果同时使用Resilience4j 和 Sentinel 就会发生 Feign 的 FallbackFactory 被两边都拦截，谁先注册谁生效，另外一个完全不工作 
但你以为两个都在保护，其实只有一个在工作。而且： 增加启动时间； 引入多个 AOP 拦截，会产生副作用（如 fallback 不触发、熔断状态不一致等）；
日志不清晰，不知道哪个组件在工作。

## 技术选型

        你使用的体系	                      推荐熔断器	                推荐负载均衡器
    Spring Cloud 官方体系（Boot 3.x）  	 ✅ Resilience4j	    ✅ Spring Cloud LoadBalancer
    Spring Cloud Alibaba（含 Nacos）	     ✅ Sentinel         	✅ Nacos LoadBalancer
    K8s + Istio 服务网格	                 可选：Envoy/LB 熔断	        Istio / Sidecar


## Feign 客户端查看

https://xie.infoq.cn/article/d17c619ade78d8f5d02d48bb5


## Feign 配置  okhttp客户端后，会自动使用 http长连接，并使用连接失败重试

## Spring Cloud 中内部微服务调用默认是 http 请求，主要通过下面三种 API：

RestTemplate：同步 http API  在 Spring Framework 6.0 (Spring Boot 3.0) 中 RestTemplate 进入维护模式，不再增加新特性。
WebClient：异步响应式 http API，推荐用于响应式场景。
三方客户端封装，例如 OpenFeign。
Spring 6.1 (Spring Boot 3.2) 推出 RestClient：一个现代化的同步 HTTP 客户端，提供流畅的 API，是 RestTemplate 的官方推荐替代品。



