package com.example.auth.config;

import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.util.Arrays;

///身份验证提供程序，实现了身份验证逻辑

/// https://xieshaohu.wordpress.com 有另外一种写法
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

    ///AuthenticationProvider会实现身份验证逻辑，接收来自AuthenticationManager的请求，
    // 并将查找用户的任务委托给UserDetailsService,然后验证PasswordEncoder接收的密码


    ///用于实现所有用户身份验证逻辑
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        String userName=authentication.getName();
        String password=String.valueOf(authentication.getCredentials());
        if ("john".equals(userName) && "12345".equals(password)) {
              UsernamePasswordAuthenticationToken tmp=new UsernamePasswordAuthenticationToken(userName, password, Arrays.asList());

              System.out.println(String.valueOf(tmp.getCredentials()));
              System.out.println(String.valueOf("+++++++++++++++++++"));
            return tmp;
        } else {
            throw new AuthenticationCredentialsNotFoundException("Error!");
        }



    }

    @Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);

    }
}
