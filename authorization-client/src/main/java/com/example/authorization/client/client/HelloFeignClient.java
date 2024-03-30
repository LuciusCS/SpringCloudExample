package com.example.authorization.client.client;


import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.service.annotation.GetExchange;

@FeignClient("authorization-resource")  ///用于表示调用的服务名
public interface HelloFeignClient {

    ///用于表示调用的微服务接口，authorization-resource 微服务下的
    @GetMapping("/")
    String getHello();
}
