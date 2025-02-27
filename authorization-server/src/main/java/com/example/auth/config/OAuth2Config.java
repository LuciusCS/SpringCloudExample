package com.example.auth.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 负责配置 Spring Security 的安全性，定义哪些端点需要认证，哪些端点可以开放，以及如何处理请求的安全性（如 CSRF、防护、JWT 验证等）。
 */
//@EnableAuthorizationServer  老版本的写法
@Configuration
@EnableWebSecurity
public class OAuth2Config {


    private final JwtDecoder jwtDecoder;

    public OAuth2Config(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }

    /**
     * defaultSecurityFilterChain 聚焦于 资源服务器 的安全配置，主要是保护 API 和资源端点，对传入的 OAuth2 令牌进行验证，用于资源服务器
     * authorizationServerSecurityFilterChain 聚焦于 授权服务器 的安全配置，处理 OAuth2 授权流程和令牌生成等相关操作，用于授权服务器
     * @param http
     * @return
     * @throws Exception
     */
    // 更多配置，如令牌存储、端点配置等
    //defaultSecurityFilterChain 更适用于资源服务器，配置了如何处理 OAuth2 令牌和 API 请求的安全性。
    //
//    @Bean
//    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/oauth2/token").permitAll() // 开放令牌端点
//                        .anyRequest().authenticated()
//                )
//                .csrf(csrf -> csrf.disable()) // 根据实际情况决定是否禁用CSRF
//                .oauth2ResourceServer(oauth2 -> oauth2.jwt()); // 如果资源服务器需要JWT验证
//
//        return http.build();
//    }

    // 授权服务器安全配置  下面是客户端通过 client id 进行登录
    // 授权服务器安全配置（处理 /oauth2/authorize, /oauth2/token 等端点）
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        /// 授权服务器需要有这一行，资源服务器不需要有这一行
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .securityMatcher("/oauth2/**")
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/oauth2/token"))
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder)));

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/userinfo", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin(form -> form
                        .loginPage("/login")
                        .permitAll()
                )
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder)));

        return http.build();
    }


    // 密码模式需要配置 AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(
            UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder
    ) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(provider);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}


