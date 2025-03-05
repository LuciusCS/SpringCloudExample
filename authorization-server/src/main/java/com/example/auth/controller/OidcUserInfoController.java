package com.example.auth.controller;


import com.example.auth.bean.User;
import com.example.auth.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/// 自定义用户信息控制器
@RestController
public class OidcUserInfoController {

    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {

        ///这部分代码有错误，后面再改
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        return Map.of(
                "sub", user.getUsername(),
                "name", user.getUsername(),
                "email", user.getEmail(),
                "roles", user.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .collect(Collectors.toList())
        );
    }

    /// 用于表示添加用户
    @PostMapping("/addUser")
    public boolean addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return  userService.save(user);
    }
}
