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
        String orderNo = map.get("orderNo");
        log.info("Received simulate_success request for order: {}", orderNo);

        // Safety check: only allow if mock is explicitly true OR we are in a dev-like
        // state
        boolean isMock = Boolean.TRUE.equals(billService.getProperties().getMock());
        if (!isMock) {
            log.warn("Attempted simulate_success but mock is disabled in config");
            throw new RuntimeException("Simulation is disabled");
        }

        callbackService.handleSimulatedSuccess(orderNo);
        log.info("Successfully handled simulate_success for order: {}", orderNo);
        return "SUCCESS";
    }
}
