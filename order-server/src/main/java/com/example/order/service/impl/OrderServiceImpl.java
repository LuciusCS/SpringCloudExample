package com.example.order.service.impl;

import com.example.order.bean.Order;
import com.example.order.repository.OrderRepository;
import com.example.order.service.OrderService;
import com.example.order.tenant.TenantContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {


    @Autowired
    OrderRepository orderRepository;


    @Override
    @Transactional
    public void save(Order order) {

        orderRepository.save(order);

        // 执行一些同步逻辑
        System.out.println("In main thread, Tenant ID: " + TenantContext.getTenantId());

        // 执行异步任务，租户 ID 会被传递到异步线程
        processAsyncTask();

    }

    @Override
    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    // 异步任务，模拟跨线程处理
    @Async
    public void processAsyncTask() {
        // 在异步任务中获取租户信息
        System.out.println("In async thread, Tenant ID: " + TenantContext.getTenantId());
    }
}
