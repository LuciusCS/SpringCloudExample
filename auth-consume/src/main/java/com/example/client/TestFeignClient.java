package com.example.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@FeignClient("auth")   ///用于表示调用的服务名
public interface TestFeignClient {


    //"dept/add"是在 auth模块下的 com.example.controller.DeptController
    // 实际 调用的是 auth服务中的 /dept/add 接口
    @PostMapping("/dept/add")
    boolean testClient();
}
