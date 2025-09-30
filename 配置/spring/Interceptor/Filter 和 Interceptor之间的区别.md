





### 给网络请求的Filter设置权重/设置先后顺序
以及为什么要设置先后顺序



### Filter 和 Interceptor 之间的区别

| 特性       | Filter（过滤器）                                                             | Interceptor（拦截器）                                           |
| -------- | ----------------------------------------------------------------------- | ---------------------------------------------------------- |
| **所在层次** | 属于 **Servlet 规范**，在 Servlet 容器层面生效（Tomcat、Jetty 等）。                     | 属于 **Spring MVC 框架**，依赖 Spring 容器。                         |
| **拦截范围** | 可以拦截所有请求，包括静态资源（HTML、CSS、JS、图片等），只要进了 Servlet 容器就能过滤。                   | 只拦截进入 Spring MVC 的请求（即经过 DispatcherServlet 的请求），对静态资源默认无效。 |
| **触发时机** | DispatcherServlet 之前，处于请求最前沿。                                           | DispatcherServlet 之后，Controller 调用之前/之后。                   |
| **使用场景** | 更偏底层的通用处理：编码过滤、防 XSS 攻击、日志记录、跨域处理（CORS）、权限认证的“前置粗过滤”。                   | 更偏业务相关的处理：登录鉴权、权限控制、请求参数校验、操作日志、性能监控、统一异常处理。               |
| **配置方式** | 依赖 `@WebFilter` 或 `FilterRegistrationBean`，在 web.xml 或 Spring Boot 中注册。 | 依赖 `HandlerInterceptor` 接口，通过 `WebMvcConfigurer` 注册。       |
| **感知能力** | 只知道 `ServletRequest`、`ServletResponse`，不了解 Spring 容器中的 Bean。            | 可以直接使用 Spring IOC 容器的 Bean（比如调用 service、查数据库）。             |


### 具体使用场景举例

* Filter 场景
   - 请求统一编码（CharacterEncodingFilter）
   - XSS 攻击过滤器（过滤 <script> 等恶意输入）
   - 跨域处理（CORS）
   - IP 黑白名单过滤
   - 压缩响应内容（GZipFilter）
   - 认证：处理用户名/密码、Token、OAuth2 登录、JWT 校验（放在 SecurityFilterChain）。 
   - 鉴权：权限检查，决定是否能访问某个 URL。 
   - 跨站安全：CSRF 防护、CORS 处理。 
   - 统一异常处理：认证失败、权限不足时直接返回 401/403。 
   - 请求上下文初始化：比如设置 SecurityContextHolder。
  
* Interceptor 场景
   - 登录校验（判断用户是否已登录）
   - 权限控制（某些接口只能管理员访问）
   - 日志记录（记录用户操作、方法调用耗时）
   - 国际化（根据用户选择切换语言）
   - 业务登录校验：比如必须绑定手机号、必须实名认证。
   - 接口级权限：对某些接口做细粒度权限判断（比角色更具体，比如订单只能操作自己的）。
   - 参数封装（比如在请求进入 Controller 前构建一个用户上下文对象）
   - 上下文设置：把 UserDetails 转换成项目内部的 UserContext，放到 ThreadLocal，Controller 直接用
   - 接口日志：记录操作人、接口耗时。
   - 多租户拦截：根据请求头/参数设置租户上下文。



Interceptor 适用于 Spring MVC 层面的操作，如请求预处理、日志记录、权限控制等。Interceptor 做与业务相关的拦截（依赖 Spring Bean）
Filter 适用于 Servlet 容器层面的操作，能处理 HTTP 请求和响应，但不能直接访问 Spring MVC 层面的组件。Filter 做通用、框架级的处理（与业务无关）


### Spring 请求处理流程（带 Filter & Interceptor）


请求进来：
Filter → Interceptor.preHandle → Controller
响应出去：
Interceptor.afterCompletion → Filter


┌─────────────────────┐
│  浏览器/客户端请求   │
└─────────┬───────────┘
          ↓
┌───────────────────┐
│   Filter 过滤器链  │  ← 属于 Servlet 层（容器级）
│ (XSS、防CORS、编码等)│
└─────────┬─────────┘
          ↓
┌───────────────────┐
│ DispatcherServlet │ ← Spring MVC 的核心
└─────────┬─────────┘
          ↓
┌───────────────────┐
│ Interceptor.pre   │  ← 进入 Controller 前
│ (登录鉴权、权限校验)│
└─────────┬─────────┘
          ↓
┌───────────────────┐
│   Controller层    │  ← 业务逻辑处理
└─────────┬─────────┘
          ↓
┌───────────────────┐
│ Interceptor.post  │  ← Controller 执行后，渲染前
│ (日志、数据加工等) │
└─────────┬─────────┘
          ↓
┌───────────────────┐
│  视图解析 & 渲染   │
└─────────┬─────────┘
          ↓
┌───────────────────┐
│ Interceptor.after │  ← 完全结束（善后）
│ (清理资源、异常处理)│
└─────────┬─────────┘
          ↓
┌───────────────────┐
│ Filter 过滤器链    │ ← 响应返回时也能处理
│ (压缩、统一Header) │
└─────────┬─────────┘
          ↓
┌─────────────────────┐
│   浏览器/客户端接收 │  
└─────────────────────┘
