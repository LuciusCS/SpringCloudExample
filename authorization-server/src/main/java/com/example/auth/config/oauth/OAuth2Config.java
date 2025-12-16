package com.example.auth.config.oauth;

import com.example.auth.config.extension.password.PasswordAuthenticationConverter;
import com.example.auth.config.extension.password.PasswordAuthenticationProvider;
import com.example.auth.config.oauth.handler.CustomAccessDeniedHandler;
import com.example.auth.config.oauth.handler.CustomAuthenticationEntryPoint;
import com.example.auth.handler.MyAuthenticationFailureHandler;
import com.example.auth.handler.MyAuthenticationSuccessHandler;
import com.example.auth.oidc.CustomOidcAuthenticationConverter;
import com.example.auth.oidc.CustomOidcAuthenticationProvider;
import com.example.auth.oidc.CustomOidcUserInfoService;
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
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * 负责配置 Spring Security 的安全性，定义哪些端点需要认证，哪些端点可以开放，以及如何处理请求的安全性（如 CSRF、防护、JWT
 * 验证等）。
 */
// @EnableAuthorizationServer 老版本的写法
@Configuration
@EnableWebSecurity
public class OAuth2Config {

        private final JwtDecoder jwtDecoder;

        private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

        private final CustomOidcUserInfoService customOidcUserInfoService;

        private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
        private final CustomAccessDeniedHandler customAccessDeniedHandler;

        public OAuth2Config(JwtDecoder jwtDecoder, OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer,
                        CustomOidcUserInfoService customOidcUserInfoService,
                        CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                        CustomAccessDeniedHandler customAccessDeniedHandler) {
                this.jwtDecoder = jwtDecoder;
                this.jwtCustomizer = jwtCustomizer;
                this.customOidcUserInfoService = customOidcUserInfoService;
                this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
                this.customAccessDeniedHandler = customAccessDeniedHandler;
        }

        // 授权服务器安全配置 下面是客户端通过 client id 进行登录
        // 授权服务器安全配置（处理 /oauth2/authorize, /oauth2/token 等端点）
        // 授权服务器配置 (authorizationServerSecurityFilterChain)
        // 是专门处理授权流程的配置，主要处理授权码、客户端凭证、token 颁发等。
        @Bean
        @Order(1)
        public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,
                        AuthenticationManager authenticationManager,
                        OAuth2AuthorizationService authorizationService,
                        OAuth2TokenGenerator<?> tokenGenerator) throws Exception {
                /// 授权服务器需要有这一行，资源服务器不需要有这一行
                /// 配置 OAuth2 授权服务器所需的默认安全设置，通常包括用户授权、客户端认证、登录等。
                OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

                http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                                // 自定义授权模式转换器(Converter)
                                .tokenEndpoint(tokenEndpoint -> tokenEndpoint
                                                .accessTokenRequestConverters(
                                                                authenticationConverters -> // <1>
                                                                // 自定义授权模式转换器(Converter)
                                                                authenticationConverters.addAll(
                                                                                List.of(
                                                                                                new PasswordAuthenticationConverter()

                                                                                )))
                                                .authenticationProviders(
                                                                authenticationProviders -> // <2>
                                                                // 自定义授权模式提供者(Provider)
                                                                authenticationProviders.addAll(
                                                                                List.of(
                                                                                                new PasswordAuthenticationProvider(
                                                                                                                authenticationManager,
                                                                                                                authorizationService,
                                                                                                                tokenGenerator))))
                                                .accessTokenResponseHandler(new MyAuthenticationSuccessHandler()) // 自定义成功响应
                                                .errorResponseHandler(new MyAuthenticationFailureHandler()) // 自定义失败响应
                                )
                                // Enable OpenID Connect 1.0 自定义
                                .oidc(oidcCustomizer -> oidcCustomizer.userInfoEndpoint(userInfoEndpointCustomizer -> {
                                        userInfoEndpointCustomizer
                                                        .userInfoRequestConverter(new CustomOidcAuthenticationConverter(
                                                                        customOidcUserInfoService));
                                        userInfoEndpointCustomizer.authenticationProvider(
                                                        new CustomOidcAuthenticationProvider(authorizationService));
                                }));

                http
                                /// 授权服务器通常只处理 /oauth2/token、/oauth2/authorize 等与 OAuth2 相关的请求。
                                .securityMatcher("/oauth2/**") // 只匹配以 /oauth2/ 开头的请求
                                .csrf(csrf -> csrf /// 针对 OAuth2 相关的端点禁用 CSRF 防护
                                                .ignoringRequestMatchers("/oauth2/token")
                                                .ignoringRequestMatchers("/jwks")
                                                .ignoringRequestMatchers("/.well-known/openid-configuration")

                                                .ignoringRequestMatchers(request -> request.getRequestURI()
                                                                .startsWith("/druid/"))) // 忽略 /oauth2/token 路径的 CSRF 防护
                                                                                         // 添加表单登录支持（关键修复）
                                .formLogin(withDefaults())
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.decoder(jwtDecoder))); // 配置 JWT 解码器

                return http.build();
        }

        /// 资源服务器配置 (defaultSecurityFilterChain) 是专门保护应用资源的配置，主要处理对 API
        /// 的授权访问，确保客户端请求中携带有效的 JWT 或其他类型的 OAuth2 令牌。
        /// 其实没必要在授权服务器中添加
        @Bean
        @Order(2)
        public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
                http
                                .exceptionHandling(
                                                exceptionHandling -> exceptionHandling
                                                                .authenticationEntryPoint(
                                                                                customAuthenticationEntryPoint)
                                                                .accessDeniedHandler(customAccessDeniedHandler) // 设置权限不足的处理

                                )// 设置自定义的 AuthenticationEntryPoint

                                .securityMatcher(request -> !request.getRequestURI().startsWith("/oauth2/") // 排除
                                                                                                            // /oauth2/
                                                                                                            // 路径的请求，使其不被当前的过滤器处理。
                                                                                                            // 这意味着该过滤器不会处理与
                                                                                                            // OAuth2
                                                                                                            // 授权相关的请求，主要保护其他资源。

                                )
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers("/css/**", "/js/**", "/webjars/**", "/images/**",
                                                                "/favicon.ico")
                                                .permitAll()

                                                .requestMatchers("/oauth2/jwks", "/.well-known/openid-configuration",
                                                                "/userinfo", "/login", "/addRegisteredClient",
                                                                "/druid/**", /// 表示上述端点可以被任何人进行访问
                                                                "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html", // ✅
                                                                                                                         // 必加这一行
                                                                "/auth/wechat/**" // 微信登录接口公开访问
                                                // "/oauth2/authorize" // 允许匿名访问授权端点（触发登录）
                                                ).permitAll()
                                                .anyRequest().authenticated())
                                // .formLogin(form -> form
                                // .loginPage("/login")
                                // .permitAll()
                                // )
                                .formLogin(withDefaults())
                                .csrf(csrf -> csrf
                                                .ignoringRequestMatchers("/.well-known/openid-configuration",
                                                                "/addRegisteredClient", "/oauth2/token", "/addUser",
                                                                "/druid/**", "/auth/wechat/**")
                                // .ignoringRequestMatchers(request ->
                                // request.getRequestURI().startsWith("/druid/"))
                                // 禁用对/addRegisteredClient接口的CSRF保护
                                )
                                .oauth2ResourceServer(oauth2 -> oauth2
                                                .jwt(jwt -> jwt.decoder(jwtDecoder))
                                                .authenticationEntryPoint(customAuthenticationEntryPoint) // 确保 OAuth2
                                                                                                          // 的过期 token
                                                                                                          // 也会交由
                                                                                                          // CustomAuthenticationEntryPoint
                                                                                                          // 处理
                                );

                return http.build();
        }

        @Bean
        OAuth2TokenGenerator<?> tokenGenerator(JWKSource<SecurityContext> jwkSource) {
                JwtGenerator jwtGenerator = new JwtGenerator(new NimbusJwtEncoder(jwkSource));
                jwtGenerator.setJwtCustomizer(jwtCustomizer);

                OAuth2AccessTokenGenerator accessTokenGenerator = new OAuth2AccessTokenGenerator();
                OAuth2RefreshTokenGenerator refreshTokenGenerator = new OAuth2RefreshTokenGenerator();
                return new DelegatingOAuth2TokenGenerator(
                                jwtGenerator, accessTokenGenerator, refreshTokenGenerator);
        }

        // 密码模式需要配置 AuthenticationManager
        @Bean
        public AuthenticationManager authenticationManager(
                        UserDetailsService userDetailsService,
                        PasswordEncoder passwordEncoder) {
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
