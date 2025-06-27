

## FilterConfig 配置类：

该类用于将自定义的过滤器注册到 Spring Boot 的过滤器链中，确保 Spring Boot 应用在处理请求时，会按照定义的顺序执行你的过滤器。
FilterRegistrationBean 是 Spring 提供的注册过滤器的工具，可以指定过滤器的 URL 映射模式、执行顺序等。