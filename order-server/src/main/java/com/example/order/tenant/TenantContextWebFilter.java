package com.example.order.tenant;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 *  这里为什么不需要加注解，也不需要进行注册
 *  Spring Boot 默认会将所有的 @Component 注册为过滤器，但过滤器在执行时需要一个正确的顺序。
 *  如果你的 TenantContextWebFilter 的顺序不正确，可能会导致请求处理异常或无法正确初始化
 *  下面的Filter 因为顺序不对，导致启动不了，因此需要使用手动注册
 *
 */
//@Component
public class TenantContextWebFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // 从请求头或请求参数中获取租户 ID
//        String tenantId = request.getParameter("tenantId");  // 或者从请求头中获取
        String tenantId = request.getHeader("tenantId");  // 从请求头中获取 tenantId
        if (tenantId != null) {
            TenantContext.setTenantId(Long.parseLong(tenantId)); // 设置租户 ID
        }

        try {
            chain.doFilter(request, response); // 继续请求
        } finally {
            TenantContext.clear(); // 请求结束后清理上下文
        }

    }

}
