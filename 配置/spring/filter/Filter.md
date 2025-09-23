

## 'javax.servlet.Filter' 与 ‘org.springframework.web.filter’ 使用方法如下代码

如果你在使用 Spring 或 Spring Boot 框架，OncePerRequestFilter 提供了更好的集成性、灵活性以及易于扩展的能力。它自动与 Spring 体系集成，并且能确保每个请求只执行一次。
'javax.servlet.Filter' 适用于传统的 Servlet 项目，或者在非 Spring 环境下运行时也能有效处理租户信息。由于没有 Spring 提供的机制，通常不如 OncePerRequestFilter 灵活和方便。


```java
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import java.io.IOException;

@WebFilter("/*")
public class TenantFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        // 初始化代码
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        
    }

    @Override
    public void destroy() {
        // 销毁代码
    }
}

```

```java

 //推荐使用

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class TenantContextWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
    
    }

}


```


