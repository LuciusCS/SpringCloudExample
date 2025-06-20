package com.example.controller;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String home(Authentication authentication){

        /// 用于获取授权信息
        String username = authentication.getName();

        LocalDateTime time=LocalDateTime.now();

        return  "Hello from the resource server! -"+ time;
    }

}
