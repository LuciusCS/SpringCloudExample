package com.example.service;


import com.example.client.TestFeignClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TestFeignService {

    @Autowired
    private TestFeignClient feignClient;


    public void  getTestFeignInfo(){
        feignClient.testClient();
    }


}
