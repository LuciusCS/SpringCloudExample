package com.example.auth.config;


import com.example.auth.config.extension.password.PasswordAuthenticationConverter;
import com.example.auth.config.extension.password.PasswordAuthenticationProvider;
import com.example.auth.handler.MyAuthenticationFailureHandler;
import com.example.auth.handler.MyAuthenticationSuccessHandler;
import com.example.auth.oidc.CustomOidcAuthenticationConverter;
import com.example.auth.oidc.CustomOidcAuthenticationProvider;
import com.example.auth.oidc.CustomOidcUserInfoService;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.lob.DefaultLobHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.jackson2.SecurityJackson2Modules;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.jackson2.OAuth2AuthorizationServerJackson2Module;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.token.*;
import org.springframework.security.web.SecurityFilterChain;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
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

    private final OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer;

    private final CustomOidcUserInfoService customOidcUserInfoService;


    public OAuth2Config(JwtDecoder jwtDecoder,OAuth2TokenCustomizer<JwtEncodingContext> jwtCustomizer,CustomOidcUserInfoService customOidcUserInfoService) {
        this.jwtDecoder = jwtDecoder;
        this.jwtCustomizer = jwtCustomizer;
        this.customOidcUserInfoService = customOidcUserInfoService;
    }


    // 授权服务器安全配置  下面是客户端通过 client id 进行登录
    // 授权服务器安全配置（处理 /oauth2/authorize, /oauth2/token 等端点）
    //授权服务器配置 (authorizationServerSecurityFilterChain) 是专门处理授权流程的配置，主要处理授权码、客户端凭证、token 颁发等。
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http,     AuthenticationManager authenticationManager,
                                                                      OAuth2AuthorizationService authorizationService,
                                                                      OAuth2TokenGenerator<?> tokenGenerator) throws Exception {
        /// 授权服务器需要有这一行，资源服务器不需要有这一行
        ///  配置 OAuth2 授权服务器所需的默认安全设置，通常包括用户授权、客户端认证、登录等。
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

                                                )
                                        )
                        )
                        .authenticationProviders(
                                authenticationProviders -> // <2>
                                        // 自定义授权模式提供者(Provider)
                                        authenticationProviders.addAll(
                                                List.of(
                                                        new PasswordAuthenticationProvider(authenticationManager, authorizationService, tokenGenerator)
                                                                                           )
                                        )
                        )
                        .accessTokenResponseHandler(new MyAuthenticationSuccessHandler()) // 自定义成功响应
                        .errorResponseHandler(new MyAuthenticationFailureHandler()) // 自定义失败响应
                )
                // Enable OpenID Connect 1.0 自定义
                .oidc(oidcCustomizer ->
                        oidcCustomizer.userInfoEndpoint(userInfoEndpointCustomizer ->
                                {
                                    userInfoEndpointCustomizer.userInfoRequestConverter(new CustomOidcAuthenticationConverter(customOidcUserInfoService));
                                    userInfoEndpointCustomizer.authenticationProvider(new CustomOidcAuthenticationProvider(authorizationService));
                                }
                        )
                );

        http
                /// 授权服务器通常只处理 /oauth2/token、/oauth2/authorize 等与 OAuth2 相关的请求。
                .securityMatcher("/oauth2/**")                        // 只匹配以 /oauth2/ 开头的请求
                .csrf(csrf -> csrf
                        .ignoringRequestMatchers("/oauth2/token"))   // 忽略 /oauth2/token 路径的 CSRF 防护
                // 添加表单登录支持（关键修复）
                .formLogin(withDefaults())
                .oauth2ResourceServer(oauth2 -> oauth2
                        .jwt(jwt -> jwt.decoder(jwtDecoder)));   // 配置 JWT 解码器

        return http.build();
    }


    /// 资源服务器配置 (defaultSecurityFilterChain) 是专门保护应用资源的配置，主要处理对 API 的授权访问，确保客户端请求中携带有效的 JWT 或其他类型的 OAuth2 令牌。
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

    @Bean
    public OAuth2AuthorizationService authorizationService(JdbcTemplate jdbcTemplate,
                                                           RegisteredClientRepository registeredClientRepository) {
        // 创建基于JDBC的OAuth2授权服务。这个服务使用JdbcTemplate和客户端仓库来存储和检索OAuth2授权数据。
        JdbcOAuth2AuthorizationService service = new JdbcOAuth2AuthorizationService(jdbcTemplate, registeredClientRepository);

        // 创建并配置用于处理数据库中OAuth2授权数据的行映射器。
        JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper rowMapper = new JdbcOAuth2AuthorizationService.OAuth2AuthorizationRowMapper(registeredClientRepository);
        rowMapper.setLobHandler(new DefaultLobHandler());
        ObjectMapper objectMapper = new ObjectMapper();
        ClassLoader classLoader = JdbcOAuth2AuthorizationService.class.getClassLoader();
        List<Module> securityModules = SecurityJackson2Modules.getModules(classLoader);
        objectMapper.registerModules(securityModules);
        objectMapper.registerModule(new OAuth2AuthorizationServerJackson2Module());
        // You will need to write the Mixin for your class so Jackson can marshall it.

        // 添加自定义Mixin，用于序列化/反序列化特定的类。
        // Mixin类需要自行实现，以便Jackson可以处理这些类的序列化。
//        objectMapper.addMixIn(SysUserDetails.class, SysUserMixin.class);
        objectMapper.addMixIn(Long.class, Object.class);

        // 将配置好的ObjectMapper设置到行映射器中。
        rowMapper.setObjectMapper(objectMapper);

        // 将自定义的行映射器设置到授权服务中。
        service.setAuthorizationRowMapper(rowMapper);

        return service;
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


