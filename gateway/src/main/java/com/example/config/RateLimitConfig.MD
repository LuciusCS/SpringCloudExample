

### Spring Cloud Gateway 内置的 RequestRateLimiter 过滤器使用 令牌桶算法 进行限流，
这个限流是通过 Redis + Lua 脚本 实现的。，应该是request_rate_limiter.lua 这一个

如果 Spring Cloud Gateway 需要限流，可以直接使用 RequestRateLimiter 结合 RedisRateLimiter，无需自己写 Lua！

请求到来时，Spring Cloud Gateway 调用 RedisRateLimiter，执行 Lua 脚本。
Lua 脚本计算是否有足够令牌：
如果有，返回 {1, 剩余令牌数}（允许请求）。
如果没有，返回 {0, 剩余令牌数}（拒绝请求）。



### Redis 集群模式下，Lua 脚本只能在 单个节点执行，跨多个分片时会有数据不一致问题。



### 后面需要改成 Sentinel或者 Redisson 支持集群

