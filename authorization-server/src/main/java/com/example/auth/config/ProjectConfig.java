package com.example.auth.config;

import com.example.auth.entity.SecurityUser;
import com.example.auth.entity.User;
import com.example.auth.services.InMemoryUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.SecurityBuilder;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;

@Configuration
public class ProjectConfig {


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
        UserDetails u = new SecurityUser(new User("john", "12345", "read"));
        List<UserDetails> users = List.of(u);
        return new InMemoryUserDetailsService(users);
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



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.httpBasic();
//        http.authorizeRequests().anyRequest().authenticated();  ///所有的请求都需要身份验证
        http.authorizeRequests().anyRequest().permitAll();  ///所有的请求都需要身份验证
        return http.build();
    }

    ///注册用户授权检查bean
//    @Bean
//    public AuthenticationProvider authenticationProvider(){
//        DaoAuthenticationProvider daoAuthenticationProvider=new DaoAuthenticationProvider();
//        daoAuthenticationProvider.setUserDetailsService(userDetailsService());
//        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
//        return  daoAuthenticationProvider;
//    }



}
