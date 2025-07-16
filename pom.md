





# 版本管理

```
    <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-dependencies</artifactId>
                <version>${spring-cloud-dependencies.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

            <dependency>
                <groupId>org.springframework.boot</groupId>
                <artifactId>spring-boot-dependencies</artifactId>
                <version>${spring-boot-dependencies.version}</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>

```
spring-cloud 和 spring-boot 指定号版本后，跟spring-boot相关的版本和跟spring-cloud相关的，其他依赖就不需要指定版本了


## Spring Boot 和 Spring Cloud 的版本管理机制。
通过引入 spring-boot-dependencies 和 spring-cloud-dependencies 的 BOM（Bill Of Materials）管理，你不需要为某些依赖显式指定版本号，因为这些 BOM 会管理相关的版本。

1. BOM（Bill Of Materials）是什么？
   在 Maven 中，BOM 是一种特殊的 POM 文件，它用于集中管理一组依赖的版本。这些版本可以被多个项目共享，从而避免在每个项目中重复指定版本号。

通过使用 BOM，你不需要在 dependency 中显式指定版本，Maven 会自动根据 BOM 中的配置来解析版本。

2. 为什么引入 spring-boot-dependencies 和 spring-cloud-dependencies？
   这两个依赖是 Spring Boot 和 Spring Cloud 提供的 BOM。通过导入这两个 BOM，你的项目能够自动继承这些框架所支持的默认版本，确保版本兼容性，并简化版本管理。

3. 如何使用 BOM 管理版本：
   在 pom.xml 中，dependencyManagement 和 dependencies 两个部分具有不同的功能：
   dependencyManagement： 用于声明和管理依赖的版本，所有依赖的版本会从这里继承，但不会自动下载依赖。
   dependencies： 这里是你实际添加到项目中的依赖，dependencyManagement 中声明的版本会自动继承。

4. 总结 引入 spring-boot-dependencies 和 spring-cloud-dependencies 之后：

你可以省略大多数 Spring Boot 和 Spring Cloud 相关依赖的版本号。
例如，你的 spring-boot-starter-web 和 spring-cloud-starter-loadbalancer 就会自动使用 BOM 中定义的版本，而不需要显式指定版本号。

spring-boot-dependencies 和 spring-cloud-dependencies 是 BOM 文件，它们负责自动管理相关依赖的版本。
 通过导入这些 BOM，你的项目可以自动使用推荐的兼容版本，不需要手动指定版本号。
你只需要在 dependencies 中指定 groupId 和 artifactId，Maven 会根据 BOM 来解析和下载正确的版本。


## 如何区分哪些依赖使用 spring-boot-starter 和 spring-cloud-starter

spring-boot-starter 用于构建常规的应用，适用于所有类型的 Java 项目，包括 Web 应用、数据库应用等。
spring-cloud-starter 是 Spring Cloud 提供的用于微服务架构的依赖，它集成了多种分布式系统的功能，比如服务发现、配置中心、负载均衡、断路器等。
如果你的项目是一个 微服务项目，并且涉及到 分布式特性，则应选择 spring-cloud-starter。否则，如果你的应用只是单体的 Web 应用，就使用 spring-boot-starter。

求类型	                    依赖类型	                                        说明
构建常规单体应用	        spring-boot-starter 	         如果你只是想构建一个普通的 Web 应用或者数据应用，选择 spring-boot-starter 相关的依赖。

构建微服务应用	        spring-cloud-starter	         如果你的应用是微服务架构的一部分，且需要分布式特性（如服务发现、负载均衡等），使用 spring-cloud-starter 相关的依赖。

服务注册与发现	    spring-cloud-starter-eureka	         使用 Eureka 实现服务注册与发现。

负载均衡	            spring-cloud-starter-ribbon
                或 spring-cloud-starter-loadbalancer	  Ribbon 或 Spring Cloud LoadBalancer 负责服务间的负载均衡。

API 网关           	spring-cloud-starter-zuul
                或 spring-cloud-starter-gateway          使用 Zuul 或 Spring Cloud Gateway 作为 API 网关。

配置中心	        spring-cloud-starter-config              使用 Spring Cloud Config 实现分布式配置管理。

服务间调用	   spring-cloud-starter-openfeign	         使用 Feign 客户端进行服务间调用。

断路器与容错机制	spring-cloud-starter-hystrix
               或 spring-cloud-starter-circuitbreaker    使用 Hystrix 或 Spring Cloud Circuit Breaker 来提供服务的容错能力。

分布式追踪	     spring-cloud-starter-sleuth	         集成 Spring Cloud Sleuth 进行分布式追踪。



## 子工程和父工程的依赖

参考 https://juejin.cn/post/7227128420358422565

如果 maven 父工程中使用的是 dependencies 标签，则子工程中可以直接使用父工程的引用。
如果父工程中使用的是 dependencyManagement 则子工程中不能直接使用父工程引入的依赖，
需要在自己的 pom 文件中引入，此时只需要添加坐标即可，不需要添加版本号。因为 dependencyManagement 对依赖进行了统一的版本管理。


用一个不恰当的例子说明：dependencyManagement就相当于餐馆的老板，老板把菜、调味品等所需要的东西备好了，
厨师 子工程 只负责使用老板已备好的东西进行出菜即可