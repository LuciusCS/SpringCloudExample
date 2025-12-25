package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 商品销售记录表 (统计维度)
 * 记录每次 OrderItem 的成交情况
 */
@Entity
@Table(name = "product_sales_record", indexes = {
        @Index(name = "idx_product_id", columnList = "productId"),
        @Index(name = "idx_artist_id", columnList = "artistId"),
        @Index(name = "idx_pay_time", columnList = "payTime")
})
@Data
public class ProductSalesRecordPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 商品ID */
    private Long productId;

    /** 商品名称快照 */
    private String productName;

    /** 画师ID/商家ID */
    private Long artistId;

    /** 关联订单ID */
    private Long orderId;

    /** 订单号 */
    private String orderNo;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 成交金额 (该项的总金额，即 OrderItem.subtotalAmount) */
    private BigDecimal amount;

    /** 购买数量 */
    private Integer quantity;
}
