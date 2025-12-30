package com.example.demo.controller;

import com.example.demo.bean.dto.OrderCreateReq;
import com.example.demo.bean.dto.OrderCreateResp;
import com.example.demo.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @PostMapping("/create")
    public OrderCreateResp create(@RequestBody OrderCreateReq req) {
        return orderService.createOrder(req);
    }

}
