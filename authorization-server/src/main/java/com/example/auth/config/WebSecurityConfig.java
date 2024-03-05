package com.example.auth.config;

import com.example.auth.entity.SecurityUser;
import com.example.auth.entity.User;
import com.example.auth.services.InMemoryUserDetailsService;

import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.JWKSecurityContext;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.MediaType;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
//import java.security.interfaces.RSAKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;


/**
 * Spring Security配置类
 */
@Configuration
@EnableWebSecurity
@EnableWebFluxSecurity
public class WebSecurityConfig {


    @Bean   ///一个用于 协议端点 的 Spring Security 过滤器链。
    @Order(1)  ///定义bean的加载顺序
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        ///OpenID Connect 1.0 在默认配置中被禁用。下面的例子显示了如何通过初始化 OidcConfigurer 来启用OpenID Connect 1.0。
        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); /// 使用OpenID Connect 1.0
        // 当未登录时访问认证端点时重定向至登录页面，默认前往登录页的uri是/login
        http.exceptionHandling((exceptions) -> exceptions.defaultAuthenticationEntryPointFor(
                        ///跳转到登陆页面
                        new LoginUrlAuthenticationEntryPoint("/login"),
                        new MediaTypeRequestMatcher(MediaType.TEXT_HTML)))
                ///接受用户和客户端的 access token
                .oauth2ResourceServer((resourceServer) -> resourceServer
                        .jwt(Customizer.withDefaults()));

        return http.build();


    }


    @Bean
    @Order(2)  ///	一个用于 认证 的 Spring Security 过滤器链。
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
//        http.httpBasic();
//        http.authorizeRequests().anyRequest().authenticated();  ///所有的请求都需要身份验证
//        http.authorizeRequests().anyRequest().permitAll();  ///所有的请求都需要身份验证，有一些需要略过的数据直接掠过

//        http
//                // 禁用basic明文验证
//                .httpBasic().disable()
//                // 前后端分离架构不需要csrf保护
//                .csrf().disable()
//                // 禁用默认登录页
//                .formLogin().disable()
//                // 禁用默认登出页
//                .logout().disable()
//                // 设置异常的EntryPoint，如果不设置，默认使用Http403ForbiddenEntryPoint
//                .exceptionHandling(exceptions -> exceptions.authenticationEntryPoint(invalidAuthenticationEntryPoint))
//                // 前后端分离是无状态的，不需要session了，直接禁用。
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//                        // 允许所有OPTIONS请求
//                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                        // 允许直接访问授权登录接口
//                        .requestMatchers(HttpMethod.POST, "/web/authenticate").permitAll()
//                        // 允许 SpringMVC 的默认错误地址匿名访问
//                        .requestMatchers("/error").permitAll()
//                        // 其他所有接口必须有Authority信息，Authority在登录成功后的UserDetailsImpl对象中默认设置“ROLE_USER”
//                        //.requestMatchers("/**").hasAnyAuthority("ROLE_USER")
//                        // 允许任意请求被已登录用户访问，不检查Authority
//                        .anyRequest().authenticated())
//                .authenticationProvider(authenticationProvider())
//                // 加我们自定义的过滤器，替代UsernamePasswordAuthenticationFilter
//                .addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);

//        http
//                .authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
//                .formLogin(Customizer.withDefaults())
//
//                .csrf(csrf -> csrf
////                        .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//                        .csrfTokenRepository(new HttpSessionCsrfTokenRepository())
//                        .csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler()));
        ///需要删除或保留
         http  .csrf(csrf -> csrf.disable());

        return http.build();
    }

    @Bean  ///	UserDetailsService 的一个实例，用于检索要认证的用户。
    public UserDetailsService userDetailsService() {
//        InMemoryUserDetailsManager userDetailsService = new InMemoryUserDetailsManager();
//
//        UserDetails user = User.withUsername("john")
//                .password("12345")
//                .authorities("read")
//                .build();
//
//        userDetailsService.createUser(user);
//
//        return userDetailsService;


//        UserDetails u = new SecurityUser(new User("john", "12345", "read"));
//        List<UserDetails> users = List.of(u);
//
//        return new InMemoryUserDetailsService(users);

        UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
                .username("user")
                .password("{noop}password")  ///好像是表示不需要加密
                .roles("USER")
                .build();


        return new InMemoryUserDetailsManager(userDetails);
//        UserDetails userDetails=Us
    }


    @Bean  ///	RegisteredClientRepository 的一个实例，用于管理客户端
    public RegisteredClientRepository registeredClientRepository() {
        RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("messaging-client")
                .clientSecret("{noop}secret")
                // 客户端认证基于请求头
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                // 配置授权的支持方式
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
//                .authorizationGrantType(AuthorizationGrantType.PASSWORD)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized")
//                .redirectUri("www.baidu.com")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("message.read")
                .scope("message.write")
                // 客户端设置，设置用户需要确认授权
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        return new InMemoryRegisteredClientRepository(registeredClient);

    }

    @Bean  ///	com.nimbusds.jose.jwk.source.JWKSource 的一个实例，用于签署访问令牌（access token）
    public JWKSource<SecurityContext> jwtSource() {
        KeyPair keyPair = generateRsaKey();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

//        RSAKey 不能使用 import java.security.interfaces.RSAKey;
//        而用import com.nimbusds.jose.jwk.RSAKey;
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();
//
        JWKSet jwkSet = new JWKSet(rsaKey);

        return new ImmutableJWKSet<>(jwkSet);

    }

    ///生成RsaKey  java.security.KeyPair 的一个实例，其 key 在启动时生成，用于创建上述 JWKSource。
    private static KeyPair generateRsaKey() {

        KeyPair keyPair;
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            keyPair = keyPairGenerator.generateKeyPair();
        } catch (Exception exception) {
            throw new IllegalStateException(exception);
        }

        return keyPair;

    }


    @Bean ///	JwtDecoder 的一个实例，用于解码签名访问令牌（access token）。
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return NoOpPasswordEncoder.getInstance();
//    }

    @Bean  //AuthorizationServerSettings 的一个实例，用于配置Spring授权服务器。
    public AuthorizationServerSettings authorizationServerSettings(){
        return  AuthorizationServerSettings.builder().build();
    }
//    void configure(HttpSecurity http) throws Exception {
//        http.httpBasic();
//        http.authorizeRequests().anyRequest().authenticated();
//
//   }


    ///注册用户授权检查bean
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        return  daoAuthenticationProvider;
//    }


}
