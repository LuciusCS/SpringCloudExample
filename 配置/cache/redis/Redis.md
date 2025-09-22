


## Redis 与 Memcached 有啥区别


由于 Redis 只使用单核，而 Memcached 可以使用多核，所以平均每一个核上 Redis 在存储小数
据时比 Memcached 性能更高。而在 100k 以上的数据中，Memcached 性能要高于 Redis。虽然
Redis 最近也在存储大数据的性能上进行优化，但是比起 Memcached，还是稍有逊色。



+ 其他区别




如果采用了主从架构，那么建议必须开启 master node 的持久化，不建议用 slave node
作为 master node 的数据热备，因为那样的话，如果你关掉 master 的持久化，可能在 master
宕机重启的时候数据是空的，然后可能一经过复制， slave node 的数据也丢了。
另外，master 的各种备份方案，也需要做。万一本地的所有文件丢失了，从备份中挑选一份
rdb 去恢复 master，这样才能确保启动的时候，是有数据的，即使采用了后续讲解的高可用
机制，slave node 可以自动接管 master node，但也可能 sentinel 还没检测到 master failure，
master node 就自动重启了，还是可能导致上面所有的 slave node 数据被清空。



Redis 的高可用架构，叫做 failover 故障转移，也可以叫做主备切换。