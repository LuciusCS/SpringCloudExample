

##  Interceptor 的使用

### 1. Interceptor

```java
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class MyInterceptor implements HandlerInterceptor {

    // 请求处理前
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("Pre Handle method is Calling");
        // 继续执行其他拦截器或目标方法
        return true;
    }

    // 请求处理后
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
        System.out.println("Post Handle method is Calling");
    }

    // 请求完成后
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        System.out.println("Request and Response is completed");
    }
}

```

### 2. 定义拦截器

方法一：在配置类中注册拦截器

```java
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new MyInterceptor())
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns("/login", "/logout");  // 排除某些路径
    }
}

```


方法二：使用 @Component 注解自动注册拦截器

```java

@Component
public class MyInterceptor implements HandlerInterceptor {
    // 方法实现同上
}
```

然后在配置类中通过注入来注册：
```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final MyInterceptor myInterceptor;

    public WebConfig(MyInterceptor myInterceptor) {
        this.myInterceptor = myInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(myInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/login", "/logout");
    }
}
```

## 拦截器的使用场景

### 认证和授权
在请求到达 Controller 之前进行认证和授权检查。比如验证请求中是否携带了有效的 Token 或者 Session 信息。

```java
@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String token = request.getHeader("Authorization");
    if (token == null || !isValidToken(token)) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        return false;  // 拦截请求
    }
    return true;  // 继续执行
}


```

###  日志记录
在请求开始前记录日志，或者在响应之后记录日志，记录请求的参数、响应时间等信息。

```java

@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    long startTime = System.currentTimeMillis();
    request.setAttribute("startTime", startTime);
    return true;
}

@Override
public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    long startTime = (Long) request.getAttribute("startTime");
    long endTime = System.currentTimeMillis();
    System.out.println("Request URI: " + request.getRequestURI() + ", Time Taken: " + (endTime - startTime) + " ms");
}

```

### 性能监控
拦截请求的时间，统计请求的执行时长，进行性能监控。

### 请求数据处理
在请求到达 Controller 之前进行数据预处理，比如修改请求的参数或增加必要的字段。
```java

@Override
public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    String tenantId = request.getHeader("Tenant-ID");
    TenantContext.setTenantId(tenantId);
    return true;
}

```
### 统一异常处理
通过拦截器统一捕获异常并进行处理，或者记录异常日志。

### Interceptor 也可以添加多个

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private LogInterceptor logInterceptor;

    @Autowired
    private AuthInterceptor authInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 先注册 log，再注册 auth
        registry.addInterceptor(logInterceptor).addPathPatterns("/**");
        registry.addInterceptor(authInterceptor).addPathPatterns("/**");
    }
}

```

### 请求执行时

```
>>> [LogInterceptor] preHandle
>>> [AuthInterceptor] preHandle
--- Controller 执行 ---
<<< [AuthInterceptor] postHandle
<<< [LogInterceptor] postHandle
<<< [AuthInterceptor] afterCompletion
<<< [LogInterceptor] afterCompletion
```


## 在 Spring WebFlux 开发过程中，不能使用传统的 Spring MVC HandlerInterceptor

HandlerInterceptor 是 Spring MVC 特有的拦截器机制，它基于 Servlet 规范，适用于传统的 Servlet 容器。
Spring WebFlux 是基于 反应式编程 的，使用的是 Reactive 栈，而不是传统的 Servlet 栈，因此 HandlerInterceptor 不适用于 WebFlux。
但是，WebFlux 提供了类似的功能，可以通过 WebFilter 和 HandlerFilterFunction 来代替传统的拦截器。

