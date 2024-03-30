package com.example.authorization.client.controller;


import com.example.authorization.client.client.HelloClient;
import com.example.authorization.client.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class AppController {

    @Autowired
    private AppService appService;

    private final HelloClient helloClient;

    public AppController(HelloClient helloClient) {
        this.helloClient = helloClient;
    }


    @GetMapping("/")
    public ResponseEntity<String>getPublicData(){
        return  ResponseEntity.ok("public data");
    }

    @GetMapping("/private-data")
    public ResponseEntity<String>getPrivateData(){
        return ResponseEntity.ok(appService.getJwtToken());
    }

    @GetMapping("hello")
    public ResponseEntity<String>sayHello(){
        return ResponseEntity.ok(helloClient.getHello());
    }



}
