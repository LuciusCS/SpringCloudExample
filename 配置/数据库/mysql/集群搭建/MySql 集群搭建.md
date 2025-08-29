

# MySQL数据库高可用性方案





# MySQL InnoDB Cluster（官方集群方案）
## 环境 

 Rocky-9.6
 
 Docker
 
 MySql 8.0
 

## 在每台机器上启动 Docker 网络（桥接网络）：
在每台机器上运行以下命令创建 Docker 网络：

bridge 在 docker-compose中使用了，下面这行可以有可以没有

```yaml
docker network create --driver=bridge mysql-cluster-net
```

## Docker 配置

创建 mysql-docker-compose1.yml  mysql-docker-compose2.yml mysql-docker-compose3.yml

需要修改 container_name、--server-id 、mysql1-data 、ports

```yaml

version: '3.8'
services:
  mysql1:
    image: mysql:8.0
    container_name: mysql1
    environment:
      MYSQL_ROOT_PASSWORD: 123456
    command: --server-id=1 --log-bin --gtid-mode=ON --enforce-gtid-consistency=TRUE --log-replica-updates=TRUE --binlog-format=ROW  --binlog_transaction_dependency_tracking=WRITESET --sql-mode=STRICT_TRANS_TABLES,NO_ZERO_DATE,NO_ZERO_IN_DATE
    ports:
      - "33061:3306"
    volumes:
      - mysql1-data:/develop/mysql/mysql1
    networks:
      - mysql-cluster

volumes:
  mysql1-data:

networks:
  mysql-cluster:
    driver: bridge

```

分别在三台电脑启动docker

```shell
 docker compose -f mysql-docker-compose1.yml up -d
```

## 在主节点进入 Docker

```shell
docker exec -it mysql1 bash
```

执行
```shell

mysqlsh --uri root@192.168.22.191:33061 --password=123456

// 进入  MySQL  10.0.2.15:33061 ssl  JS > 这种模式后，操作才有效

// 下面这三行分别在每一个容器中运行
dba.configureInstance('root@192.168.22.191:33061', {password: '123456'});
dba.configureInstance('root@192.168.22.191:33062', {password: '123456'});
dba.configureInstance('root@192.168.22.191:33063', {password: '123456'});


dba.createCluster('MyCluster');
var cluster = dba.getCluster('MyCluster');


cluster.addInstance('root@192.168.22.191:33062', {password: '123456', recoveryMethod: 'clone'});
// 在执行完上上面一行后，会出现Please make sure the MySQL server at 'f55e2248d255:3306' is restarted and call <Cluster>.rescan() to complete the process.
// 容器会被杀死，并不能主动restart ，需要另开shell窗口，启动容器，否则需要执行 cluster.rescan()才可消除错误

cluster.addInstance('root@192.168.22.191:33063', {password: '123456', recoveryMethod: 'clone'});

```


## MySql Router 配置

MySql Router 中

```shell

services:
  mysql-router:
    image: mysql/mysql-router:8.0
    container_name: mysql-router
    # 这些只是用于通过启动检查，指向任一健康节点即可
    environment:
      MYSQL_HOST: 192.168.22.191      # 集群中的一个 MySQL 实例的外部地址
      MYSQL_PORT: 33061                # MySQL 服务的端口
      MYSQL_USER: root                   # 连接集群的用户名
      MYSQL_PASSWORD: 123456      # 连接集群的密码
      MYSQL_ROUTER_USER: router
      MYSQL_ROUTER_PASSWORD: 123456
      # MYSQL_INNODB_CLUSTER_MEMBERS: "192.168.22.191:33061,192.168.22.191:33062,192.168.22.191:33063"  # 集群中的所有节点的外部地址
      MYSQL_CREATE_ROUTER_USER: "false"    # 是否自动创建 MySQL Router 用户
      # MYSQL_INNODB_CLUSTER_MEMBERS: 3
    volumes:
      - /develop/mysql/config/mysqlrouter.conf:/etc/mysqlrouter/mysqlrouter.conf  # 挂载自定义配置文件      
    ports:
      - "6446:6446"  # MySQL Router 端口
      - "6447:6447"  # RW端口
      - "6448:6448"  # RO端口
    networks:        # 应该与MySql集群使用同一个网络
      - mysql-cluster 
    restart: unless-stopped

networks:
  mysql-cluster:
    driver: bridge


```







## 其他
使用上面 docker-compose1 启动后可能会出现数据库连不上的错误
需要在主节点上执行下面的操作, 在非主节点上执行不生效

```shell
docker exec -it mysql1 bash
mysql -u root -p
ALTER USER 'root'@'%' IDENTIFIED WITH mysql_native_password BY '123456';
```

## 查询数据库中的表操作

```shell

docker exec -it mysql1 bash
mysql -u root -p
SHOW DATABASES;
```


### 问题解决

在使用Docker 运行mysql搭建集群时，节点会报告 容器id: 3306，实际需要报告宿主机 192.168.22.191:33063

Having extra GTID events is not expected, and it is recommended to investigate this further and ensure that the data 
can be removed prior to choosing the clone recovery method. Clone based recovery selected through the recoveryMethod 
option Validating instance configuration at 192.168.22.191:33063...

This instance reports its own address as 0fc7df1f4932:3306 

为什么不能报告自己的 地址是 192.168.22.191:33063



### 参考资料

https://www.cnblogs.com/Sol-wang/p/17122439.html