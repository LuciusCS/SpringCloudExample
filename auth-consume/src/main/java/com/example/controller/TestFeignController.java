package com.example.controller;


import com.example.service.TestFeignService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class TestFeignController {

    @Autowired
    TestFeignService feignService;

    @PostMapping("/test/feign")
    private boolean testFeign(){
        feignService.getTestFeignInfo();
        return true;
    }
}
