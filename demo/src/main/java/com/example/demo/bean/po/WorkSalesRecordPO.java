package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 作品收益记录表 (统计维度)
 * 记录每次 OrderContent (具体作品) 的成交/包含情况
 */
@Entity
@Table(name = "work_sales_record", indexes = {
        @Index(name = "idx_work_id", columnList = "artistWorkId"),
        @Index(name = "idx_product_id", columnList = "productId"),
        @Index(name = "idx_pay_time", columnList = "payTime")
})
@Data
public class WorkSalesRecordPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 作品ID */
    private Long artistWorkId;

    /** 作品名称快照 */
    private String workName;

    /** 所属商品ID */
    private Long productId;

    /** 画师ID */
    private Long artistId;

    /** 关联订单ID */
    private Long orderId;

    /** 订单号 */
    private String orderNo;

    /** 支付时间 */
    private LocalDateTime payTime;

    /** 收益金额 (该具体作品在订单中的分配金额) */
    private BigDecimal amount;
}
