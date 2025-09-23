


Spring WebFlux 中的 WebFilter 的确能涵盖 Spring MVC 中的 OncePerRequestFilter 和 HandlerInterceptor 的大部分功能，
尤其是在处理请求和响应时。它能够在请求到达处理器之前和响应返回之前执行相关的业务逻辑，如权限校验、上下文设置、日志记录等。
如果你的应用是 WebFlux 环境，则应该优先使用 WebFilter，它不仅能处理同步请求，还能够支持异步请求，这使得它能够更好地与反应式流和 WebFlux 的架构兼容。
在 Spring MVC 环境 中，OncePerRequestFilter 和 HandlerInterceptor 是不可替代的，它们分别负责同步请求处理和请求/响应拦截，而 WebFlux 环境的特性要求使用 WebFilter。