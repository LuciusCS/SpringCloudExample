

Spring Cloud Gateway 内置的 RequestRateLimiter 过滤器使用 令牌桶算法 进行限流，而这个限流是通过 Redis + Lua 脚本 实现的。

应该是request_rate_limiter.lua 这一个