package com.example.order.controller;


import com.example.order.bean.Order;
import com.example.order.config.rebbitmq.MqProductor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order/controller")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private MqProductor mqProductor;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @PostMapping("/order")
    public String createOrder(@RequestBody Order order) {
        // 发送订单信息到 RabbitMQ 队列

        mqProductor.convertAndSend(order);
        logger.debug("订单创建开始"); // 开发环境可见
        logger.info("订单创建成功，订单ID: {}"); // 生产环境可见
        logger.error("订单创建异常"); // 错误日志
        return "Order received, processing asynchronously";
    }

}
