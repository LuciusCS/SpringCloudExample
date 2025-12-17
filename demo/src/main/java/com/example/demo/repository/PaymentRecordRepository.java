package com.example.demo.repository;

import com.example.demo.bean.po.PaymentRecordPO;
import org.springframework.data.jpa.repository.JpaRepository;

public  interface PaymentRecordRepository extends JpaRepository<PaymentRecordPO, Long> {

    // 根据外部支付单号查找支付记录
    PaymentRecordPO findByOutTradeNo(String outTradeNo);
}