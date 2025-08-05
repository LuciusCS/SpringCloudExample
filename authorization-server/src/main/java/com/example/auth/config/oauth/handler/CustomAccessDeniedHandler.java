package com.example.auth.config.oauth.handler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * 这个没起作用暂时没有测试
 */
@Component
public class CustomAccessDeniedHandler  implements AccessDeniedHandler {

//    @Override
//    public void handle(HttpServletRequest request, HttpServletResponse response,
//                       AccessDeniedException accessDeniedException) throws IOException {
//        response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 状态码
//        response.setContentType("application/json");
//        response.getWriter().write("{\"error\": \"Access Denied\"}");
//    }

    /**
     * 用于处理权限不足,
     * @param request
     * @param response
     * @param accessDeniedException
     * @throws IOException
     * @throws ServletException
     */

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // 403 状态码
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Access Denied\"}");
    }
}
