



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