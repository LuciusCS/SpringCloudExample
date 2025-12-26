package com.example.demo.repository;

import com.example.demo.bean.po.OrderContentPO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderContentRepository extends JpaRepository<OrderContentPO, Long> {
}
