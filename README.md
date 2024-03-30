


## Nacos

使用版本： nacos-server-2.2.1


nacos配置，

nacos启动方式，单机启动 

windows:   startup.cmd -m standalone

linux:  sh startup.sh -m standalone

nacos 2.2.1 版本需要单独设置 conf目录下 application.properties 

  ```
    nacos.core.auth.enabled=true
    nacos.core.auth.plugin.nacos.token.secret.key=VGhpc0lzTXlDdXN0b21TZWNyZXRLZXkwMTIzNDU2Nzg=
    
    nacos.core.auth.server.identity.key=abcde
    nacos.core.auth.server.identity.value=abcde
    
  ```

### Nacos配置

1、需要创建一个名字为  QD的Service ，在bootstrap.yml中配置         cluster-name: QD 

2、auth-dev.yaml 配置信息需要在public下进行创建，在QD下创建会注入失败；或者在 cloud.nacos.config.namespace namespace设置QD的id


#### 在bootstrap.yaml 中配置nacos的路经后，微服务启动时还是使用 127.0.0.1:8848 注册nacos

需要对模块依赖进行clean

### mysql配置

1、创建spring_cloud_db 数据库，其中有一张表  dept



### 调试用的接口

直接通过auth微服务调用            192.168.99.106:8001/dept/add

通过 auth-consume 调用auth接口   192.168.99.106:8002/test/feign

通过 gateway调用auth接口        192.168.99.106:8003/auth-consume/test/feign
                         或者  192.168.99.106:8003/auth/dept/add



### 使用 springdoc 替代swagger

springdoc 访问的端口为微服务的端口


### 如果程序不能运行

有时候清理一下maven就可以


### Nacos一直请求本地路经

重启清零/maven清理