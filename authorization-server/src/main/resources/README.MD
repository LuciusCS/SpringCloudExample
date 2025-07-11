  


## spring-cloud-starter-bootstrap 和 spring-cloud-starter

spring-cloud-starter-bootstrap 主要用于 外部配置管理，而
spring-cloud-starter 是 Spring Cloud 的核心 Starter，包含了一些默认功能。

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

参考文档：Spring Security OAuth2.0 配置文档

https://blog.csdn.net/u013737132/article/details/134024381

https://gitee.com/youlaitech/youlai-mall/tree/master



# swagger 访问路径
http://127.0.0.1:8003/swagger-ui/index.html
或者 http://127.0.0.1:8003/swagger-ui.html

需要在资源服务器配置 "/swagger-ui/**", "/swagger-ui.html"  

否则访问需要进行login验证



## OAuth2 支持的登录方式

用户通过

操作类型	                          操作位置	                             解释
OAuth2 登录客户端信息 CRUD	认证服务器（Authorization Server） 	 这些信息属于客户端应用配置，认证服务器负责授权和令牌发放。
通过 OCID 登录的用户信息 CRUD	资源服务器（Resource Server）	         用户数据是资源服务器管理的，资源服务器使用令牌来控制对用户数据的访问。



## 现在使用的数据库 spring_cloud_db

使用的数据表 oauth2_client、 user 、 role 、permission 、user_role、role_permission

