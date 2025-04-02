

方式	                                     适用场景	                                           是否需要 routes 配置
DiscoveryClient          自动发现 (spring.cloud.gateway.discovery.locator.enabled=true)
                            只需要按 serviceId 访问（如 /user-service/**），不需要自定义路径	            ❌ 不需要
Nacos 配置 routes	     需要自定义 /user/** -> user-service 规则，或者使用 filters 进行限流、鉴权	    ✅ 需要
本地 application.yml                 配置 routes	规则固定，不会经常变更                                	✅ 需要（但不推荐，更新需要重启）



如果你的 Gateway 只是简单代理 Nacos 的服务，可以不开启 routes，但如果有路径映射、限流、鉴权等需求，
还是推荐使用 Nacos 配置动态路由，方便后续扩展和管理。




application.yml 即使不配置下面的路由信息，Nacos也会自动配置

```xml


    # 让gateway从nacos中获取服务信息，如果没有这一个不会进行转发
    gateway:
      discovery:
        locator:
          enabled: true #使用服务发现路由
          lower-case-service-id: true
      routes:
        - id: authorization-resource-server
          uri: http://localhost:8006
          predicates:
            - Path=/api/**
        - id: seckill-server
          uri: http://localhost:8005
          predicates:
            - Path=/api/**
```