




# Feign客户端调用超时后，Okhttp会进行重试，所以失败返回 SeckillFeignClientFallback 不能调用


Feign 的超时异常通常会触发重试机制，而 fallback 方法只有在请求完全失败时才会被调用，即 Feign 没有重试时才会触发 fallback。

# 要使Fallback生效
禁用 Feign 默认的重试机制，确保超时时直接进入 fallback。
确保 Feign 配置正确，包括 fallback 类和方法的实现。
调整 Feign 客户端的超时设置，避免因超时而触发重试。