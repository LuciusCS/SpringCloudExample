package com.example.order.controller;


import com.example.order.feign.SeckillFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/seckillFeign")
public class SeckillFeignController {

    @Autowired
    SeckillFeignClient feignClient;

    @RequestMapping(method = RequestMethod.GET, value = "/hello")
    String requestHello(){

        return  feignClient.getTestHello();
    }
}
