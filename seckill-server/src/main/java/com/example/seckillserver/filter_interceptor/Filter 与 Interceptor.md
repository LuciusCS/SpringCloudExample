## Filter 与 Interceptor 的区别详解及使用场景分析

在 Java Web 开发中，Filter（过滤器）和 Interceptor（拦截器）都是处理请求的重要组件，但它们有着本质的区别和不同的使用场景。下面我将详细解释它们的区别并通过示例说明各自的使用场景。

# 1. 核心区别概述

特性 Filter (过滤器)                             Interceptor (拦截器)
规范/框架 Servlet 规范 (J2EE 标准)                     Spring MVC 框架
作用范围 Web 容器层面 Spring 容器层面
执行位置 Servlet 前后 Controller 前后
依赖关系 不依赖 Spring 依赖 Spring 容器
获取 Bean 需要特殊处理 直接注入 Spring Bean
异常处理 只能处理 Filter 链中的异常 可结合 ControllerAdvice 全局处理
配置方式 web.xml 或 @WebFilter 实现 HandlerInterceptor 接口
功能 适用于权限检查、请求日志记录等与控制器紧密相关的任务。 适用于跨请求的功能，如日志记录、请求和响应修改、性能监控等。

# 2. 区别

Filter 处理底层HTTP协议相关事务

Interceptor 处理业务相关的横切关注点

Filter 在 Interceptor 之前执行

# 3. 使用场景对比

## Filter 适用场景：

### 请求/响应编码处理

```
request.setCharacterEncoding("UTF-8");
response.setCharacterEncoding("UTF-8");
```

### 跨域请求处理

```
response.setHeader("Access-Control-Allow-Origin", "*");
response.setHeader("Access-Control-Allow-Methods", "GET,POST");
```

### 请求日志记录

```
System.out.println("Request URI: " + request.getRequestURI());
```

### XSS防护

```
public class XssFilter implements Filter {
@Override
public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) {
HttpServletRequest request = (HttpServletRequest) req;
chain.doFilter(new XssRequestWrapper(request), res);
}
}
```

### GZIP压缩

```
if (acceptsGZip(request)) {
responseWrapper = new GZipResponseWrapper(response);
chain.doFilter(request, responseWrapper);
responseWrapper.finish();
} else {
chain.doFilter(request, response);
}
```

## Interceptor 适用场景：

### 权限验证

```
if (!userService.hasPermission(request)) {
response.sendError(403, "Forbidden");
return false;
}
```

### 请求耗时监控

```
long startTime = System.currentTimeMillis();
request.setAttribute("startTime", startTime);

// 在afterCompletion中
long endTime = System.currentTimeMillis();
long executeTime = endTime - startTime;
log.info("Request took: {}ms", executeTime);
```

### 全局参数处理

```
@Override
public void postHandle(...) {
modelAndView.addObject("version", "1.0.0");
}

```

### 接口限流

```
@Override
public boolean preHandle(...) {
String ip = request.getRemoteAddr();
if (rateLimiter.exceedLimit(ip)) {
response.sendError(429, "Too many requests");
return false;
}
return true;
}
```

### API版本控制

```
String version = request.getHeader("API-Version");
RequestVersionContext.setVersion(version);
```