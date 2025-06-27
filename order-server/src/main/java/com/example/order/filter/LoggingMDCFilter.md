



## 过滤器（LoggingMDCFilter）：
LoggingMDCFilter 是一个 Servlet 过滤器，实现了 javax.servlet.Filter 接口。
过滤器的作用是在请求被处理之前或之后，或者在响应被发送回客户端之前，执行一些预处理或后处理逻辑。
你可以在 LoggingMDCFilter 中添加日志、身份认证、IP 拦截、请求数据验证等操作。

## FilterConfig 配置类：
该类用于将自定义的过滤器注册到 Spring Boot 的过滤器链中，确保 Spring Boot 应用在处理请求时，会按照定义的顺序执行你的过滤器。
FilterRegistrationBean 是 Spring 提供的注册过滤器的工具，可以指定过滤器的 URL 映射模式、执行顺序等。