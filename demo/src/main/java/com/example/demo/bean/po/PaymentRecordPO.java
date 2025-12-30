package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "payment_record")
public class PaymentRecordPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_no", length = 64)
    private String orderNo;

    @Column(name = "out_trade_no", length = 64, unique = true)
    private String outTradeNo;

    @Column(name = "transaction_id", length = 64)
    private String transactionId;

    /**
     * WECHAT, ALIPAY
     */
    @Column(name = "pay_type", length = 20)
    private String payType;

    @Column(name = "pay_amount", precision = 10, scale = 2)
    private BigDecimal payAmount;

    /**
     * 0: UNPAID, 1: PAID, 2: CLOSED, 3: REFUND
     * See PayStatus.java
     */
    @Column(name = "pay_status")
    private Integer payStatus;

    @Column(name = "raw_notify", columnDefinition = "TEXT")
    private String rawNotify;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "pay_time")
    private LocalDateTime payTime;

    @PrePersist
    public void init() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
    }
}
