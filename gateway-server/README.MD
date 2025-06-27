

Spring Cloud Gateway 内置的 RequestRateLimiter 过滤器使用 令牌桶算法 进行限流，而这个限流是通过 Redis + Lua 脚本 实现的。

应该是request_rate_limiter.lua 这一个



# gateway-server 权限控制, 相当于是 OAuth2 资源服务器

相当于是authorization-resource-server (已经删除)
