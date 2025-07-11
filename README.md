

## 端口设置

authorization-client          : 8001
authorization-resource-server : 8002
authorization-server          : 8003
gateway-server         : 8004
order                  : 8005
skill-server           : 8006



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


### mysql配置

1、创建spring_cloud_db 数据库，其中有一张表  dept

### 使用 springdoc 替代swagger

springdoc 访问的端口为微服务的端口


### 如果程序不能运行

有时候清理一下maven就可以


### Nacos一直请求本地路经

重启清零/maven清理

### 配置优先级冲突

‌配置优先级冲突‌：命令行参数 > 环境变量 > YAML 文件 > @Configuration 类