package com.example.order.feign;


import org.springframework.stereotype.Component;

@Component
public class SeckillFeignClientFallback implements SeckillFeignClient{
    @Override
    public String getTestHello() {
        return "Fallback 回调类";
    }
}
