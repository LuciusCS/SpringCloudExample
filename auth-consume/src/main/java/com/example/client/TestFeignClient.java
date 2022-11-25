package com.example.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("auth")
public interface TestFeignClient {


    //"dept/add"是在 auth模块下的 com.example.controller.DeptController
    @PostMapping("/dept/add")
    boolean testClient();
}
