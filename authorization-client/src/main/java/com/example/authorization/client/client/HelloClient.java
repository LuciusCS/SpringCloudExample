package com.example.authorization.client.client;


import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.HttpExchange;

///用于请求测试，这里要使用openfeign，
@HttpExchange("http://localhost:8006")   ///这个地址是authorization-server的地址
public interface HelloClient {

    @GetExchange("/")
    String getHello();

}
