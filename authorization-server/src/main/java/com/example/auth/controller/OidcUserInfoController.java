package com.example.auth.controller;


import com.example.auth.bean.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

/// 自定义用户信息控制器
@RestController
public class OidcUserInfoController {

    @GetMapping("/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {
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
}
