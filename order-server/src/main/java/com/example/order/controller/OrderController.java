package com.example.order.controller;


import com.example.common.utils.Snowflake;
import com.example.order.bean.Order;
import com.example.order.config.rabbitmq.MqProductor;
import com.example.order.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Optional;


@RestController
@RequestMapping("/order/controller")
@Tag(name = "OrderController",description = "用于订单管理")
public class OrderController {

    private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

    @Autowired
    private MqProductor mqProductor;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Autowired
    private OrderService orderService;

    @Operation(summary = "创建订单，并将订单信息发送到RabbitMQ")
    @PostMapping("/order")
    public String createOrder(@RequestBody Order order) {
        // 发送订单信息到 RabbitMQ 队列
        mqProductor.convertAndSend(order);
        logger.debug("订单创建开始"); // 开发环境可见
        logger.info("订单创建成功，订单ID: {}"); // 生产环境可见
        logger.error("订单创建异常"); // 错误日志

        // 将订单信息保存至数据库

        order.setId(new Snowflake().nextId());

        /// order 中的 tenantId 会自动通过 TenantEntityListener 进行赋值

        orderService.save(order);



        return "Order received, processing asynchronously";
    }

    @Operation(summary = "根据id获取订单信息")
    @GetMapping("/findById")
    public Optional<Order>findByOrderId(@RequestParam Long id){
        return  orderService.findById(id);
    }



    @Operation(summary = "测试雪花算法")
    @GetMapping("/")
    public  Long getSnowflake(){
        return new Snowflake().nextId();
    }

}
