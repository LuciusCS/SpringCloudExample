





### 给网络请求的Filter设置权重/设置先后顺序
以及为什么要设置先后顺序


| 特性   | `Filter`                         | `Interceptor`                             |
| ---- | -------------------------------- | ----------------------------------------- |
| 作用范围 | 全局，适用于所有 Servlet 请求              | 仅适用于 Spring MVC 请求                        |
| 生命周期 | 处理请求和响应的过程                       | 只会在请求到达 Controller 和响应处理前后执行              |
| 注册方式 | 通过 `web.xml` 或 `@WebFilter` 注解注册 | 通过 `WebMvcConfigurer` 或 `@Component` 注解注册 |
| 配置位置 | 传统的 Servlet 容器                   | Spring 配置文件                               |
| 执行顺序 | 先于拦截器执行（Filter -> Interceptor）   | 在 Controller 方法执行之前                       |



Interceptor 适用于 Spring MVC 层面的操作，如请求预处理、日志记录、权限控制等。
Filter 适用于 Servlet 容器层面的操作，能处理 HTTP 请求和响应，但不能直接访问 Spring MVC 层面的组件。
在 Spring 项目中，Interceptor 是处理业务逻辑的一种重要工具，使用它可以灵活地对请求和响应进行各种操作。
