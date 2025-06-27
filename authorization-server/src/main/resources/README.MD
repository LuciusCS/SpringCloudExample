  
```
<dependency>
     <groupId>org.springframework.cloud</groupId>
     <artifactId>spring-cloud-starter-bootstrap</artifactId>
</dependency>

和      
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter</artifactId>
</dependency>

有什么区别

```

spring-cloud-starter-bootstrap 主要用于 外部配置管理，而 
spring-cloud-starter 是 Spring Cloud 的核心 Starter，包含了一些默认功能。

1️⃣ spring-cloud-starter-bootstrap（已不推荐使用 🚫）

作用：
主要用于 bootstrap.yml 或 bootstrap.properties 加载外部配置，例如：
Nacos
Consul
Spring Cloud Config Server
允许 Spring Boot 在应用启动前 读取外部配置。
问题：

Spring Boot 2.4+ 之后，bootstrap.yml 已经被弃用，官方推荐直接使用 application.yml 代替。
推荐做法：
直接在 application.yml 里使用 spring.config.import
不需要 再引入 spring-cloud-starter-bootstrap


## 配置文件优先级

优先级由高到低： bootstrap.properties >  bootstrap.yml > application.properties > application.yml

## 配置共享
1、同一个微服务不同环境间共享配置  
使用auth.yaml不带dev的文件名
2、不同微服务之间共享配置
在bootstrap.yaml中配置

```agsl

   spring.cloud.nacos.config.shared-configs: all-service.yaml  微服务之间共享的配置文件
   spring.cloud.nacos.config.refresh-enabled: true              微服务之前共享的配置文件要实现动态配置刷新

```

参考文档：Spring Security OAuth2.0 配置文档

https://blog.csdn.net/u013737132/article/details/134024381

https://gitee.com/youlaitech/youlai-mall/tree/master