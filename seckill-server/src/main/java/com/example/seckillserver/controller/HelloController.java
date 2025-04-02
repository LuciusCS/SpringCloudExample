package com.example.seckillserver.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api")
public class HelloController {

    @GetMapping("/hello")
    public String home(){
        LocalDateTime time=LocalDateTime.now();

        return  "Hello from the resource server! -"+ time;
    }

}
