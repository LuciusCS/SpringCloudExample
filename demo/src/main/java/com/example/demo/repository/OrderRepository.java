package com.example.demo.repository;

import com.example.demo.bean.po.OrderPO;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OrderRepository extends JpaRepository<OrderPO, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select o from OrderPO o left join fetch o.items where o.orderNo = :orderNo")
    Optional<OrderPO> findByOrderNoForUpdate(String orderNo);

    Optional<OrderPO> findByOrderNo(String orderNo);
}
