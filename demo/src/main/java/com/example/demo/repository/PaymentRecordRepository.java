package com.example.demo.repository;

import com.example.demo.bean.po.PaymentRecordPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentRecordRepository extends JpaRepository<PaymentRecordPO, Long> {
    Optional<PaymentRecordPO> findByOutTradeNo(String outTradeNo);
    Optional<PaymentRecordPO> findByOrderNo(String orderNo);
}
