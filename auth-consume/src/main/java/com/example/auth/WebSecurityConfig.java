package com.example.auth;

///用于依赖配置


import com.example.security.JWTRequestFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTRequestFilter jwtRequestFilter;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
        final List<GlobalAuthenticationConfigurerAdapter> configurers = new ArrayList<>();
        configurers.add(new GlobalAuthenticationConfigurerAdapter() {
            @Override
            public void configure(AuthenticationManagerBuilder auth) throws Exception {

                auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//                super.configure(auth);
            }
        });

        return authConfig.getAuthenticationManager();

    }

    private void sharedSecurityConfiguration(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf(AbstractHttpConfigurer::disable).cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
                .sessionManagement(
                        httpSecuritySessionManagementConfigurer -> {
                            httpSecuritySessionManagementConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                        }

                );
    }


    @Bean
    public SecurityFilterChain securityFilterChainGlobalAPI(HttpSecurity httpSecurity) throws Exception {


        ///这里的user和admin 跟UserAccessController 中的@RequestMapping("user")  user对应，
        // 以及AdminAccessController 中的 @RequestMapping("admin")
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("user","admin").authorizeHttpRequests(auth->{
            auth.anyRequest().authenticated();
        }).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return  httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChainGlobalAdminAPI(HttpSecurity httpSecurity)throws  Exception{
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("/admin/**").authorizeHttpRequests(
                auth->{
                    auth.anyRequest().hasRole("ADMIN");
                }
        ).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public SecurityFilterChain securityFilterChainGlobalUserProfileAPI(HttpSecurity httpSecurity)throws  Exception{
        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("user/profile").authorizeHttpRequests(auth -> {
            auth.anyRequest().hasRole("USER");
        }).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }



    ///应该会有其他写法
    @Bean
    public SecurityFilterChain securityFilterChainLoginAPI(HttpSecurity httpSecurity) throws Exception {

        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("/user/authenticate").authorizeHttpRequests(auth -> {
            auth.anyRequest().permitAll();
        }).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }


    ///应该会有其他写法
    @Bean
    public SecurityFilterChain securityFilterChainRegisterAPI(HttpSecurity httpSecurity) throws Exception {

        sharedSecurityConfiguration(httpSecurity);
        httpSecurity.securityMatcher("/user/register").authorizeHttpRequests(auth -> {
            auth.anyRequest().permitAll();
        }).addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration configuration = new CorsConfiguration();

        configuration.setAllowedOrigins(Collections.singletonList("*"));
        configuration.setAllowedMethods(Collections.singletonList("*"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));

        configuration.addAllowedOrigin("*");
        configuration.addAllowedHeader("*");
        configuration.addAllowedMethod("*");


        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();

        source.registerCorsConfiguration("/**", configuration);



        return source;

    }



//    在Spring开发中，允许跨域（CORS）通常是为了方便开发和调试的需要，而框架默认不允许跨域是出于安全性的考虑。下面详细解释这两个原因：
//
//    1. 开发时允许跨域的原因
//        a. 前后端分离开发
//            在现代的Web开发中，前端和后端往往是分离的。前端可能运行在一个不同的服务器或端口上，而后端API则在另一个服务器或端口上。这种情况下，前端在开发过程中需要访问后端的API，就会涉及跨域请求。
//        b. 便于调试
//            在开发阶段，允许跨域可以使开发者方便地进行前后端联调，快速验证和测试功能，而不必每次都考虑跨域问题。
//        c. 统一接口管理
//            开发过程中，前端开发者和后端开发者可能会频繁变更和测试接口。如果每次都因为跨域问题而进行额外配置，会增加开发的复杂性和时间成本。
//    2. 框架默认不允许跨域的原因
//        a. 安全性考虑
//           跨域资源共享涉及浏览器的同源策略（Same-Origin Policy），这是Web安全的基础。默认不允许跨域请求可以有效防止跨站请求伪造（CSRF）等安全攻击。
//        b. 防止数据泄露
//           默认不允许跨域请求可以防止恶意网站通过JavaScript向你的服务器发送请求，从而保护你的数据免受外部攻击。
//        c. 保护用户隐私
//            默认不允许跨域请求可以防止未经授权的域访问用户的敏感信息，保护用户隐私。
//   如何安全地配置跨域
//    在开发阶段可以宽松地配置CORS，但在生产环境中需要谨慎配置，通常建议：
//    限制允许的来源：只允许特定的可信来源进行跨域请求。
//    限制允许的方法：只允许必要的HTTP方法（如GET、POST等）。
//    限制允许的头信息：只允许必要的HTTP头信息。
//    启用凭据支持：根据需要启用或禁用凭据（cookies等）的传递，但要确保安全配置。
//    例如，在生产环境中，CORS配置可能会类似于以下代码

//    @Bean
//    public CorsConfigurationSource corsConfigurationSource() {
//        CorsConfiguration configuration = new CorsConfiguration();
//        configuration.setAllowedOrigins(Arrays.asList("https://trustedwebsite.com"));
//        configuration.setAllowedMethods(Arrays.asList("GET", "POST"));
//        configuration.setAllowedHeaders(Arrays.asList("Authorization", "Content-Type"));
//        configuration.setAllowCredentials(true); // 根据需要启用或禁用
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", configuration);
//        return source;
//    }
}
