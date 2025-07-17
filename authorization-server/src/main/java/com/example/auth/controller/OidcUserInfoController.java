package com.example.auth.controller;


import com.example.auth.bean.User;
import com.example.auth.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/// 自定义用户信息控制器
@RestController
public class    OidcUserInfoController {

    @Autowired
    private UserDetailsServiceImpl userService;
    @Autowired
    PasswordEncoder passwordEncoder;
    @GetMapping("/userinfo")
    public Map<String, Object> userInfo(@AuthenticationPrincipal Jwt jwt) {

        ///这部分代码有错误，后面再改
//        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//        return Map.of(
//                "sub", user.getUsername(),
//                "name", user.getUsername(),
//                "email", user.getEmail(),
//                "roles", user.getAuthorities().stream()
//                        .map(GrantedAuthority::getAuthority)
//                        .collect(Collectors.toList())
//        );
        return Map.of(
                "sub", jwt.getClaims().get("sub"),
                "roles",jwt.getClaims().get("roles")
//                "email", jwt.getClaimAsString("email"),
//                "email", jwt.getClaimAsString("email"),
//                "email", jwt.getClaimAsString("email"),
//                "roles", jwt.getClaimAsStringList("roles")
        );
    }

    @PutMapping("/update")
    public  void update(@RequestBody User user){
        userService.update(user);
    }
    /// 用于表示获取所有的用户信息
    @GetMapping("/getAllUser")
    public List<User> getAllUser(){
        return  userService.getAllUser();
    }


    /// 用于表示添加用户
    @PostMapping("/addUser")
    public boolean addUser(@RequestBody User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        return  userService.save(user);
    }
}
