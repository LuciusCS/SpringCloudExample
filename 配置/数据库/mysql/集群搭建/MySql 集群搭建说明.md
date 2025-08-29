


## Mysql集群节点间访问使用 `容器id:3060` 的形式
如果有多台主机  `容器id:3036`  的访问形式会造成集群搭建失败

所以需要再每一个 mysql1.cnf 中添加下面的配置

```shell
[mysqld]
server-id = 1
bind-address = 0.0.0.0  # 允许所有IP连接
group_replication_start_on_boot = off
group_replication_local_address = "主机1IP:33061"
group_replication_group_seeds = "主机1IP:33061,主机2IP:33061,主机3IP:33061"
group_replication_single_primary_mode = ON
group_replication_enforce_update_everywhere_checks = OFF
```





## MySql Router 配置说明 1

环境变量 MYSQL_HOST, MYSQL_PORT, MYSQL_USER, MYSQL_PASSWORD 只是用于初始引导（bootstrap）
和通过Docker镜像的启动检查。MySQL Router连接到集群其他节点的能力并不依赖于这些环境变量，而是依赖于以下两个关键机制：

### 1、MySQL用户权限的全局性
   在MySQL InnoDB Cluster中，所有节点的用户账户和权限是自动同步的。这是通过Group Replication机制实现的。

当您在一个节点（如 192.168.22.191）上创建或修改用户时：

这个变更会被写入二进制日志(binlog)

Group Replication会自动将这些变更传播到集群中的所有其他节点

所有节点都会应用这些变更，保持用户权限的一致性

因此，如果您在节点1上使用 root 用户和密码 123456 能够连接，那么同样的凭据在所有其他节点上也一定有效。

### 2、MySQL Router的工作机制

MySQL Router通过以下方式发现和连接集群中的其他节点：

#### 初始阶段：元数据缓存引导

```shell
[metadata_cache:mycluster]
bootstrap_server_addresses=mysql://root:123456@192.168.22.191:33061
user=router
password=123456
metadata_cluster=mycluster
ttl=0.5

```

Router使用这里的配置连接到您指定的引导节点(192.168.22.191:33061)，然后：

查询 mysql_innodb_cluster_metadata 架构获取集群拓扑信息

发现集群中的所有节点及其状态(主/从)

将这些信息缓存到内存中

#### 运行阶段：使用专用路由账户

当Router需要将客户端连接路由到其他节点时，它不使用环境变量中的root凭据，而是使用配置文件中指定的专用账户：
下面的配置暂时未再 mysqlrouter.conf中进行添加

```shell
[metadata_cache:mycluster]
router_id=1
bootstrap_server_addresses=mysql://root:123456@192.168.22.191:33061
user=router   # 专用路由账户用户名
password=123456 # 专用路由账户密码 
metadata_cluster=mycluster
ttl=0.5
```

## MySql Router 配置说明2



