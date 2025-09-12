package com.example.order.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    // 快速接口，用于测试最大吞吐量
    @GetMapping("/fast")
    public String fast() {
        return "ok";
    }

    // 慢接口，用于测试并发上限
    @GetMapping("/slow")
    public String slow() throws InterruptedException {
        Thread.sleep(1000); // 模拟业务耗时 1 秒
        return "ok";
    }
}