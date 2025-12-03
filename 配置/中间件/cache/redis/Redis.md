

## Redis 与 Memcached 有啥区别


由于 Redis 只使用单核，而 Memcached 可以使用多核，所以平均每一个核上 Redis 在存储小数
据时比 Memcached 性能更高。而在 100k 以上的数据中，Memcached 性能要高于 Redis。虽然
Redis 最近也在存储大数据的性能上进行优化，但是比起 Memcached，还是稍有逊色。


| 特性          | **Redis**                                     | **Memcached**               |
| ----------- | --------------------------------------------- | --------------------------- |
| **数据结构**    | 支持丰富的数据结构（如字符串、哈希、列表、集合、有序集合、位图等）             | 仅支持键值对（字符串类型）               |
| **持久化**     | 支持持久化（RDB、AOF）                                | 不支持持久化，仅存储在内存中              |
| **集群模式**    | 支持集群（Redis Cluster）                           | 不支持集群（但可以通过分片实现分布式）         |
| **数据过期机制**  | 支持设置过期时间，支持精确的超时控制                            | 支持设置过期时间，但超时控制不如 Redis 精确   |
| **事务支持**    | 支持事务（通过 MULTI、EXEC 命令实现）                      | 不支持事务                       |
| **内存管理**    | 支持内存回收策略（如 LRU、LFU）                           | 支持简单的 LRU（最近最少使用）策略         |
| **支持的数据类型** | 字符串、哈希、列表、集合、有序集合、位图、HyperLogLog、Geospatial 等 | 仅支持简单的键值对存储                 |
| **可用性与可靠性** | 提供高可用方案（主从复制、哨兵模式、Redis Cluster）              | 无内建的高可用方案，需要自己实现分布式容错       |
| **性能**      | 性能良好，稍低于 Memcached，但支持更多功能                    | 性能更高（比 Redis 快），适用于简单的缓存场景  |
| **语言支持**    | 支持多种语言（Java、Python、C、Go 等）                    | 也支持多种语言（Java、Python、C、Go 等） |


- 选择 Redis：如果你需要 高可用性、复杂的数据结构 支持，或者对数据有持久化的需求，Redis 是首选。
- 选择 Memcached：如果你的需求只是 简单的缓存，并且对 性能要求非常高，Memcached 是一个更合适的选择。


## Redis 集群部署

如果采用了主从架构，那么建议必须开启 master node 的持久化，不建议用 slave node
作为 master node 的数据热备，因为那样的话，如果你关掉 master 的持久化，可能在 master
宕机重启的时候数据是空的，然后可能一经过复制， slave node 的数据也丢了。
另外，master 的各种备份方案，也需要做。万一本地的所有文件丢失了，从备份中挑选一份
rdb 去恢复 master，这样才能确保启动的时候，是有数据的，即使采用了后续讲解的高可用
机制，slave node 可以自动接管 master node，但也可能 sentinel 还没检测到 master failure，
master node 就自动重启了，还是可能导致上面所有的 slave node 数据被清空。


## Redis 获取热点数据
1.  **客户端收集**：在客户端进行统计（如 AtomicLong），上报到监控系统。
2.  **代理层收集**：如果使用 Twemproxy 或 Codis，可以在代理层收集。
3.  **Redis 自带命令**：
    - `redis-cli --hotkeys` (需要 maxmemory-policy 为 LFU)
    - `MONITOR` 命令（采样分析，生产慎用）
    - `hotkeys` 参数 (Redis 4.0+)

## Redis 监控
推荐使用 **Prometheus + Grafana + Redis Exporter**。
- **Redis Exporter**：采集 Redis 指标（内存、QPS、连接数等）。
- **Grafana**：展示可视化大屏。

## Redis 常用命令
- `INFO`：查看服务器状态、内存、客户端连接等。
- `CLIENT LIST`：查看当前连接的客户端。
- `SLOWLOG GET`：查看慢查询日志。
- `CONFIG GET *`：查看配置参数。