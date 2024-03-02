package com.example.auth.config;

import com.example.auth.entity.SecurityUser;
import com.example.auth.entity.User;
import com.example.auth.services.InMemoryUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

import java.util.List;
import java.util.UUID;


/**
 * Spring Security配置类
 */
@Configuration
//@EnableWebSecurity
public class WebSecurityConfig {


    @Bean
    @Order(1)  ///定义bean的加载顺序
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity http) throws Exception {

        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(http);

        http.getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults()); /// 使用OpenID Connect 1.0

        http.exceptionHandling((exceptions) -> exceptions.authenticationEntryPoint(
                        ///跳转到登陆页面
                        new LoginUrlAuthenticationEntryPoint("/login")))
                ///接受用户和客户端的 access token
                .oauth2ResourceServer(OAuth2ResourceServerConfigurer::jwt);

        return http.build();


    }


    @Bean
    @Order(2)
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
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

        http.authorizeHttpRequests((authorize) -> authorize.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults());

        return http.build();
    }

    @Bean
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

        UserDetails userDetails = org.springframework.security.core.userdetails.User.withDefaultPasswordEncoder()
                .username("user")
                .password("password")
                .roles("USER")
                .build();
        return new InMemoryUserDetailsManager(userDetails);
//        UserDetails userDetails=Us
    }


    @Bean
    public RegisteredClientRepository registeredClientRepository(){
        RegisteredClient registeredClient=RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("messaging-client")
                .clientSecret("{noop}secret")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
                .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
                .redirectUri("http://127.0.0.1:8080/authorized")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .scope("message.read")
                .scope("message.write")
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
                .build();

        return  new InMemoryRegisteredClientRepository(registeredClient);

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return NoOpPasswordEncoder.getInstance();
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
