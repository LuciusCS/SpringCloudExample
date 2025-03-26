

Sentinel 适用于 限流、网关流控、动态规则管理。
Resilience4j 适用于 熔断、降级、线程隔离、重试。Netflix Hystrix 替代者，提供 熔断、限流、重试、缓存，轻量级、基于 Java 8 函数式编程
🚀 如果你用 Spring Cloud Alibaba，推荐 Sentinel！
🚀 如果你用 Spring Cloud 官方组件，推荐 Resilience4j！





功能                      	Sentinel	                                     Resilience4j
限流（Rate Limiting）	✅ 内置多种流控规则（QPS、线程数、排队等待等）	✅ 令牌桶、漏桶限流
熔断（Circuit Breaker）	✅ 支持错误率、RT 等熔断策略	                ✅ 三种熔断策略（错误率、慢调用、异常比例）
降级（Degrade）         	✅ 运行时动态配置，支持 RT、异常比等	            ✅ 配置灵活，结合 Retry 实现
隔离（Bulkhead）        	❌（不支持线程池隔离）	                        ✅ 支持信号量隔离、线程池隔离
重试（Retry）	        ❌（需自己实现）	                            ✅ 内置 Retry 机制
缓存（Cache）	        ❌	                                        ✅ 内置缓存支持
动态规则变更	            ✅ 支持（可结合 Nacos、ZooKeeper）	        ❌（仅支持静态配置）
管理界面（Dashboard）	    ✅ 提供完整可视化 Dashboard	                ❌（无 UI，需手动配置 Prometheus/Grafana）
Spring Cloud 支持	    ✅ Spring Cloud Alibaba                  	✅ Spring Cloud 生态
社区与维护	            阿里主导，国内企业使用广泛	                    Netflix Hystrix 替代者，社区活跃



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


