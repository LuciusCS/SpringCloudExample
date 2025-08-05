package com.example.auth.config.oauth.handler;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

//    @Override
//    public void commence(HttpServletRequest request, HttpServletResponse response,
//                         AuthenticationException authException) throws IOException {
//
//
//    }

    /**
     * 用于表示验证码过期
     * @param request
     * @param response
     * @param authException
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
        // 如果是 JWT 令牌过期异常
        if (authException.getCause() instanceof JwtException) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 状态码
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Token expired, please login again\"}");
        } else {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);  // 401 状态码
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized access\"}");
        }
    }
}
