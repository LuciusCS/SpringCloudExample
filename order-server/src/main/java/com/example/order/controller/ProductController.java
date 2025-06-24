package com.example.order.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * 用于更新Redis中的库存
 */

@RestController
@RequestMapping("/product")
public class ProductController {

    @GetMapping("/hello")
    public  String hello(){
        return "hello";
    }
}
