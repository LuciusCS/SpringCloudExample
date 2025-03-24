  
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