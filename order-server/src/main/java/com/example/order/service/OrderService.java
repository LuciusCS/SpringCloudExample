package com.example.order.service;

import com.example.order.bean.Order;

import java.util.Optional;

public interface OrderService {

    void save(Order order);

    Optional<Order> findById(Long id);
}
