

# 在公网服务中，无论是网页、移动端还是 IoT，只要不是极端的低频、一次性请求场景，默认使用 HTTP 长连接是更高效、更合理的选择。
微服务之间的调用也使用HTTP 长连接


# tomcat配置

```

server:
  tomcat:
    max-keep-alive-requests: 100  # 每个连接最多复用请求数
    keep-alive-timeout: 60s       # 空闲多久断开
```


## 微服务之间调用开启

Feign 客户端开启连接池 + KeepAlive

```
feign:
  httpclient:
    enabled: true
  client:
    config:
      default:
        connectTimeout: 5000
        readTimeout: 5000
```
并搭配使用 Apache HttpClient（支持连接池复用）：

```

@Bean
public HttpClient httpClient() {
    return HttpClientBuilder.create()
            .setMaxConnTotal(200)
            .setMaxConnPerRoute(50)
            .setKeepAliveStrategy((response, context) -> 60 * 1000) // keep-alive时间
            .build();
}
```

# WebClient 示例
```
@Bean
public WebClient webClient() {
    return WebClient.builder()
            .clientConnector(new ReactorClientHttpConnector(
                HttpClient.create().keepAlive(true)))
            .build();
}
```


## 微服务之间的调用（服务内网）


✅ 一、微服务之间的调用（服务内网）

推荐方式：
统一使用带连接池的 HTTP 客户端，例如：

Feign + Apache HttpClient（配置连接池 + KeepAlive 策略）
WebClient + Reactor Netty（默认支持连接复用）


✅ 二、客户端（浏览器、移动App）与服务端的调用（公网）

📌 是否使用长连接，需要根据以下维度判断：
            情况	                                是否推荐用 HTTP 长连接
单个客户端频繁访问接口（App、IoT 设备）	    ✅ 推荐，节省建立连接时间，提高吞吐
客户端与服务端之间有 WebSocket 实时交互	    ✅ 已是长连接场景
HTTP/2 通信（如 gRPC 或启用 HTTP/2）	    ✅ 推荐，HTTP/2 天生复用连接
客户端偶尔访问（如浏览器用户访问网页）	    ✅ 浏览器默认开启 keep-alive，服务端也应保持
每次请求的数据很大或处理时间长	           ⚠️ 小心连接被长期占用，可配置超时与连接上限