package com.example.demo.repository;

import com.example.demo.bean.po.OrderItemPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItemPO, Long> {
}
