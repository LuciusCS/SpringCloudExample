package com.example.order.controller;

import com.example.order.bean.dto.PrometheusAlert;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PrometheusAlertController {

    @PostMapping("/webhook")
    public ResponseEntity<String> receiveAlert(@RequestBody PrometheusAlert payload) {
        // 从告警数据中获取所需的信息
        for (PrometheusAlert.Alert alert : payload.getAlerts()) {
            String alertname = alert.getLabels().get("alertname");
            String severity = alert.getLabels().get("severity");
            String instance = alert.getLabels().get("instance");
            String description = alert.getAnnotations().get("description");
            String summary = alert.getAnnotations().get("summary");

            // 处理告警：例如，打印告警信息
            System.out.println("告警名称: " + alertname);
            System.out.println("严重性: " + severity);
            System.out.println("实例: " + instance);
            System.out.println("描述: " + description);
            System.out.println("摘要: " + summary);
        }

        // 返回成功响应
        return ResponseEntity.ok("告警接收成功");
    }
}
