package com.example.auth.config;


import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
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
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 负责配置 Spring Security 的安全性，定义哪些端点需要认证，哪些端点可以开放，以及如何处理请求的安全性（如 CSRF、防护、JWT 验证等）。
 */
//@EnableAuthorizationServer  老版本的写法
@Configuration
@EnableWebSecurity
public class OAuth2Config {


    private final JwtDecoder jwtDecoder;

    public OAuth2Config(JwtDecoder jwtDecoder) {
        this.jwtDecoder = jwtDecoder;
    }


    // 授权服务器安全配置  下面是客户端通过 client id 进行登录
    // 授权服务器安全配置（处理 /oauth2/authorize, /oauth2/token 等端点）
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {
        /// 授权服务器需要有这一行，资源服务器不需要有这一行
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http
                .securityMatcher("/oauth2/**")                        // 只匹配以 /oauth2/ 开头的请求
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/oauth2/token"))   // 忽略 /oauth2/token 路径的 CSRF 防护
                // 添加表单登录支持（关键修复）
                .formLogin(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder)));   // 配置 JWT 解码器

        return http.build();
    }


    @Bean
    @Order(2)
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher(request ->
                        !request.getRequestURI().startsWith("/oauth2/") // 排除 /oauth2/ 路径
                )
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/userinfo", "/login", "/addRegisteredClient"
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
                        .ignoringRequestMatchers("/addRegisteredClient", "/oauth2/token","/addUser") // 禁用对/addRegisteredClient接口的CSRF保护
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


