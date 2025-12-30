package com.example.demo.controller;

import com.example.demo.service.BillService;
import com.example.demo.service.PayCallbackService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/pay")
@RequiredArgsConstructor
public class PaymentController {

    private final BillService billService;
    private final PayCallbackService callbackService;

    @PostMapping("/prepay")
    public Map<String, Object> prepay(@RequestBody Map<String, String> map) {
        String orderNo = map.get("orderNo");
        return billService.prepay(orderNo);
    }

    @PostMapping("/simulate_success")
    public String simulateSuccess(@RequestBody Map<String, String> map) {
        if (!Boolean.TRUE.equals(billService.getProperties().getMock())) {
            throw new RuntimeException("Simulation is disabled in production environment");
        }
        String orderNo = map.get("orderNo");
        callbackService.handleSimulatedSuccess(orderNo);
        return "SUCCESS";
    }
}
