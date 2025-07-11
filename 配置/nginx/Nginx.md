


# 在公网服务中，无论是网页、移动端还是 IoT，只要不是极端的低频、一次性请求场景，默认使用 HTTP 长连接是更高效、更合理的选择。
  微服务之间的调用也使用HTTP 长连接

# Nginx 配置

```

keepalive_timeout 65;    # 每个连接最多复用请求数
keepalive_requests 100;  # 空闲多久断开
```