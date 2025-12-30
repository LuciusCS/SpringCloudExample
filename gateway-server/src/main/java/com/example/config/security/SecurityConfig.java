package com.example.config.security;

import com.example.properties.SecurityProperties;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder;
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.server.SecurityWebFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    /**
     * 有一个同名的 SecurityProperties 是 org.springframework.boot.autoconfigure.security.SecurityProperties;
     * 下面这一个用的是自定的 SecurityProperties
     *
     */
//    @Resource
//    private SecurityProperties securityProperties;
//    @Autowired
//    private ReactiveJwtDecoder jwtDecoder; // 替代传统的 JwtDecoder

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        return http
                .csrf(csrf -> csrf
                        .disable() // WebFlux 中通常直接禁用，或按需忽略路径配置
                )
                .authorizeExchange(exchange -> exchange
                        // 放行的端点
//                        .pathMatchers(
//                                "/.well-known/openid-configuration",
//                                "/userinfo",
//                                "/login",
//                                "/oauth2/**", //  建议放开整个 /oauth2/** 路径
//                                "/authorization-server/**", // 显式允许该路径
//                                "/addRegisteredClient",
//                                "/druid/**",
//                                "/auth/wechat/**"
//                        ).permitAll()
                        // 其他所有请求需要认证
                        .anyExchange().permitAll()
//                        .anyExchange().authenticated()

                        /**
                         * 下面的数据用于在nacos配置成功后，允许访问
                         */
                        /**
                         *        securityProperties.getPermitAll().forEach(request -> {
                         *                         if (request.getMethod() != null) {
                         *                             authorizeExchangeCustomizer
                         *                                     .pathMatchers(HttpMethod.valueOf(request.getMethod().name()), request.getUri())
                         *                                         .permitAll();
                         *                         } else {
                         *                             authorizeExchangeCustomizer
                         *                                     .pathMatchers(request.getUri())
                         *                                         .permitAll();
                         *                         }
                         *                     });
                         */

                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt
                                .jwtDecoder(jwtDecoder())
                        )
                )
                // 表单登录（默认登录页）已经充当资源服务器了，就不需要表单登录
//                .formLogin(withDefaults())
                .build();
    }

    /**
     * 它定义了 Gateway（或资源服务器）如何解析和校验 JWT Token 的方式。
     * http://127.0.0.1:8003/oauth2/jwks 是启动authorization-server 后的启动地址
     * @return
     */
    @Bean
    public ReactiveJwtDecoder jwtDecoder() {
        /// 这里的jwk uri 来自于授权服务器
        return NimbusReactiveJwtDecoder.withJwkSetUri("http://127.0.0.1:8003/oauth2/jwks").build(); // 指定 JWK URL
    }

}
