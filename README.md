


## Nacos

使用版本： nacos-server-2.2.1


nacos配置，

nacos启动方式，单机启动 

windows:   startup.cmd -m standalone

linux:  sh startup.sh -m standalone


### Nacos配置

1、需要创建一个名字为  QD的Service ，在bootstrap.yml中配置         cluster-name: QD 

2、auth-dev.yaml 配置信息需要在public下进行创建，在QD下创建会注入失败；或者在 cloud.nacos.config.namespace namespace设置QD的id


### mysql配置

1、创建spring_cloud_db 数据库，其中有一张表  dept



### 调试用的接口

直接通过auth微服务调用            192.168.99.106:8001/dept/add

通过 auth-consume 调用auth接口   192.168.99.106:8002/test/feign

通过 gateway调用auth接口        192.168.99.106:8003/auth-consume/test/feign
                         或者  192.168.99.106:8003/auth/dept/add

