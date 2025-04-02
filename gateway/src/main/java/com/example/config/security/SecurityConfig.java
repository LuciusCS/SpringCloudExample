//package com.example.config.security;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//@Configuration
//public class SecurityConfig {
//
//
//    @Bean
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
////        http
////                .securityMatcher(request ->
////                        !request.getRequestURI().startsWith("/oauth2/") // 排除 /oauth2/ 路径
////                )
////                .authorizeHttpRequests(authorize -> authorize
////                                .requestMatchers("/userinfo", "/login", "/addRegisteredClient" /// 表示上述端点可以被任何人进行访问
//////                                "/oauth2/authorize" // 允许匿名访问授权端点（触发登录）
////                                ).permitAll()
////                                .anyRequest().permitAll()
////                );
//        http
//                // 禁用CSRF保护（可选，根据需求决定）
//                .csrf(csrf -> csrf.disable())
//                // 配置请求授权规则
//                .authorizeHttpRequests(authorize -> authorize
//                        // 允许所有请求（包括所有路径）
//                        .anyRequest().permitAll()
//                );
//        return http.build();
//    }
//}
