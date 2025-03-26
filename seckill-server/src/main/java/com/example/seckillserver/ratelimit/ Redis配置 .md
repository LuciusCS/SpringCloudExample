

## Redis 中  Lettuce 和 Redisson 的配置项

1、基本配置

```yaml

data:
  redis:
    host: localhost         # Redis 服务器地址，默认是 localhost
    port: 6379              # Redis 端口，默认是 6379
    password: 123456        # Redis 密码，如果没有密码则可以为空
    database: 1             # Redis 数据库编号，默认是 0，Redis 支持 16 个数据库（0-15）
    timeout: 3000ms         # 连接超时，单位为毫秒

```

2、Lettuce 连接池配置

```yaml

lettuce:
  pool:
    max-active: 8          # 最大连接数，控制 Redis 连接池可以提供的最大连接数
    max-idle: 8            # 最大空闲连接数，控制 Redis 连接池中的最大空闲连接数
    min-idle: 0            # 最小空闲连接数，控制 Redis 连接池中最小空闲连接数
    max-wait: 3000         # 最大等待时间，单位毫秒，表示获取 Redis 连接时的最大等待时间
```

3、Redission 配置

```yaml

redisson:
  server:
    type: standalone       # Redis 连接类型，standalone（单机模式），master-slave（主从模式），sentinel（哨兵模式），cluster（集群模式）
    database: 0            # 使用的 Redis 数据库，默认为 0 ，有0-15
    address: cdh1:6379     # Redis 主机地址和端口，格式为 ip:port，可以配置多个地址，逗号分隔
    password: admin        # Redis 密码，如果没有设置密码，可以省略
    port: 6379             # Redis 端口，默认为 6379

```

Redission高级配置

```yaml

redisson:
  threads: 16                    # 设置 Redisson 客户端的工作线程数
  nettyThreads: 32                # 设置 Netty 的线程数，Netty 是 Redisson 的通信框架
  codec: org.redisson.codec.JsonJacksonCodec  # 设置 Redis 数据编解码器，默认是 Jackson 编解码器
  transportMode: NIO              # 连接模式，NIO 是默认值，表示使用非阻塞 I/O，Redisson 也支持 EPOLL
  readMode: MASTER_SLAVE          # 设置读取模式，MASTER_SLAVE 表示主从模式，SLAVE_SYNC 表示从服务器同步读取
  subscriptionMode: REDIS       # 设置订阅模式，REDIS 表示使用 Redis 的发布/订阅功能
```


Lettuce 与 Redisson 比较

Lettuce
优点：
异步和反应式支持：Lettuce 支持异步操作和反应式编程（基于 Netty），非常适合高并发和低延迟场景。
线程安全：Lettuce 是线程安全的，多个线程可以共享同一个连接实例。
轻量级：Lettuce 主要提供基本的 Redis 操作，适合一般的 Redis 用法。
简单配置：Lettuce 配置简单，通常不需要额外的复杂设置。
缺点：
功能相对简单：Lettuce 仅提供 Redis 的基本操作，不包括像 Redisson 那样的高级功能（如分布式锁、分布式集合等）。
不支持高级数据结构：Lettuce 不支持像 Redisson 那样的分布式集合、分布式队列等高级数据结构。

Redisson  需要进行仔细查看
优点：
丰富的功能：Redisson 提供了大量 Redis 的高级功能，如分布式锁、分布式队列、分布式集合、分布式计数器等。
多种连接模式：支持单机、主从、集群、哨兵等多种 Redis 连接模式，适合更复杂的 Redis 架构。
高级数据结构：Redisson 支持多种分布式数据结构，如分布式 Map、Set、List 等，可以解决一些特殊的分布式场景。
缺点：
重量级：Redisson 相较于 Lettuce 要复杂得多，且功能繁杂，适合需要高级功能的场景。
性能开销：Redisson 封装了很多功能，可能会有一些额外的性能开销，尤其是在高并发场景下，Lettuce 可能会更加高效。
总结
如果只需要基本的 Redis 功能，推荐使用 Lettuce，它更加轻量，性能高效，适用于标准的 Redis 使用场


