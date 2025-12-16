package com.example.demo.bean.po;
import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 支付流水 = 对账凭证 = 幂等核心
 */
@Entity
@Table(name = "payment_record",
        uniqueConstraints = @UniqueConstraint(columnNames = "outTradeNo"))
@Data
public class PaymentRecordPO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 订单号 */
    private String orderNo;

    /** 第三方支付单号 */
    private String transactionId;

    /** 商户订单号（微信 out_trade_no） */
    private String outTradeNo;

    /** 支付渠道 */
    private String payType; // WECHAT / ALIPAY

    /** 支付金额 */
    private BigDecimal payAmount;

    /** 支付状态 */
    private Integer payStatus;

    /** 回调原始报文 */
    @Column(columnDefinition = "text")
    private String rawNotify;

    private LocalDateTime payTime;
}
