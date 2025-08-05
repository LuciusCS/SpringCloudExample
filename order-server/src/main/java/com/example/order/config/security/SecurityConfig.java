package com.example.order.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /// 资源服务器配置 (defaultSecurityFilterChain) 是专门保护应用资源的配置，主要处理对 API 的授权访问，确保客户端请求中携带有效的 JWT 或其他类型的 OAuth2 令牌。
    /// 其实没必要在授权服务器中添加
    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                //参考资源服务器中的配置
                .securityMatcher(request ->
                                !request.getRequestURI().startsWith("/oauth2/") // 排除 /oauth2/ 路径的请求，使其不被当前的过滤器处理。
                        // 这意味着该过滤器不会处理与 OAuth2 授权相关的请求，主要保护其他资源。

                )
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/images/**", "/favicon.ico").permitAll()
                                .requestMatchers( "/oauth2/jwks","/.well-known/openid-configuration","/druid/**", /// 表示上述端点可以被任何人进行访问
                                        "/swagger-ui/**","/v3/api-docs/**",  "/swagger-ui.html"
//                                "/oauth2/authorize" // 允许匿名访问授权端点（触发登录）
                                ).permitAll()
                                .anyRequest().authenticated()
                )

                .csrf(csrf -> csrf
                                .ignoringRequestMatchers("/.well-known/openid-configuration","/addRegisteredClient", "/oauth2/token", "/addUser", "/druid/**")
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder())
//                        .authenticationEntryPoint(customAuthenticationEntryPoint) // 确保 OAuth2 的过期 token 也会交由 CustomAuthenticationEntryPoint 处理
                ));

        return http.build();
    }
    /**
     * 它定义了 Gateway（或资源服务器）如何解析和校验 JWT Token 的方式。
     * http://127.0.0.1:8003/oauth2/jwks 是启动authorization-server 后的启动地址
     * @return
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withJwkSetUri("http://127.0.0.1:8003/oauth2/jwks").build();
    }


}
