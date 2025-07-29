package com.example.order.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

public class SecurityConfig {

    /// 资源服务器配置 (defaultSecurityFilterChain) 是专门保护应用资源的配置，主要处理对 API 的授权访问，确保客户端请求中携带有效的 JWT 或其他类型的 OAuth2 令牌。
    /// 其实没必要在授权服务器中添加
    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                //参考资源服务器中的配置
//                .exceptionHandling(
//                        exceptionHandling -> exceptionHandling
//                                .authenticationEntryPoint(customAuthenticationEntryPoint)
//                                .accessDeniedHandler(customAccessDeniedHandler)  // 设置权限不足的处理
//
//                )// 设置自定义的 AuthenticationEntryPoint

                .securityMatcher(request ->
                                !request.getRequestURI().startsWith("/oauth2/") // 排除 /oauth2/ 路径的请求，使其不被当前的过滤器处理。
                        // 这意味着该过滤器不会处理与 OAuth2 授权相关的请求，主要保护其他资源。

                )
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/images/**", "/favicon.ico").permitAll()

                                .requestMatchers( "/oauth2/jwks","/.well-known/openid-configuration", "/userinfo", "/login", "/addRegisteredClient", "/druid/**", /// 表示上述端点可以被任何人进行访问
                                        "/swagger-ui/**","/v3/api-docs/**",  "/swagger-ui.html"             // ✅ 必加这一行
//                                "/oauth2/authorize" // 允许匿名访问授权端点（触发登录）
                                ).permitAll()
                                .anyRequest().authenticated()
                )
//                .formLogin(form -> form
//                        .loginPage("/login")
//                        .permitAll()
//                )
                .formLogin(withDefaults())
                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/.well-known/openid-configuration","/addRegisteredClient", "/oauth2/token", "/addUser", "/druid/**")
//                                .ignoringRequestMatchers(request -> request.getRequestURI().startsWith("/druid/"))
                        // 禁用对/addRegisteredClient接口的CSRF保护
                )
//                .oauth2ResourceServer(oauth2 -> oauth2
//                        .jwt(jwt -> jwt.decoder(jwtDecoder))
//                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 确保 OAuth2 的过期 token 也会交由 CustomAuthenticationEntryPoint 处理
//                )
        ;

        return http.build();
    }


}
