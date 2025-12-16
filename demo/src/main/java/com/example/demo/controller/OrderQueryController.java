package com.example.demo.controller;
import com.example.demo.bean.dto.OrderDetailDTO;
import com.example.demo.service.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderQueryController {
    private final OrderQueryService orderQueryService;

    @GetMapping("/detail/{orderNo}")
    public OrderDetailDTO detail(@PathVariable String orderNo) {
        return orderQueryService.getOrderDetail(orderNo);
    }
}
