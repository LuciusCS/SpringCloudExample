/*
 * File: src\main\java\com\taylor\oauth2\controllers\BookController.java
 * Project: spring-security-oauth2-client
 * Created Date: Monday, March 18th 2024, 4:47:28 pm
 * Author: Rui Yu (yurui_113@hotmail.com)
 * -----
 * Last Modified: Friday, 22nd March 2024 8:02:12 pm
 * Modified By: Rui Yu (yurui_113@hotmail.com>)
 * -----
 * Copyright (c) 2024 Rui Yu
 * -----
 * HISTORY:
 * Date                     	By       	Comments
 * -------------------------	---------	----------------------------------------------------------
 * Monday, March 18th 2024	Rui Yu		Initial version
 */

package com.example.authorization.client.controller;


import com.example.authorization.client.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequestMapping("/book")
public class BookController {

//    @Autowired
//    private RestTemplate secureRestTemplate;
//
//    @GetMapping("/data")
//    public String fetchData() {
//        // 请求会自动携带 Token
//        ResponseEntity<String> response = secureRestTemplate.exchange(
//                "http://127.0.0.1:8003/helloTestController/hello",
//                HttpMethod.GET,
//                null,
//                String.class
//        );
//        return response.getBody();
//    }


    @Autowired
    ResourceService resourceService;


    @GetMapping("/data")
    public String fetchData() {
//        // 请求会自动携带 Token
//        ResponseEntity<String> response = secureRestTemplate.exchange(
//                "http://127.0.0.1:8003/helloTestController/hello",
//                HttpMethod.GET,
//                null,
//                String.class
//        );
//        return response.getBody();

        return  resourceService.fetchProtectedResource();
    }
}