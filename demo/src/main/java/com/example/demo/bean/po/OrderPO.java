package com.example.demo.bean.po;
import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 订单主表 OrderPO（交易维度）
 */
@Entity
@Table(name = "orders")
@Data
public class OrderPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 业务订单号 */
    @Column(unique = true, nullable = false, length = 64)
    private String orderNo;

    /** 下单用户 */
    private Long userId;

    /** 画师ID（冗余，避免反查） */
    private Long artistId;

    /** 订单类型：SINGLE / BOX / COMBO */
    private String orderType;

    /** 原价 */
    private BigDecimal originalAmount;

    /** 优惠金额 */
    private BigDecimal discountAmount;

    /** 实付金额 */
    private BigDecimal payAmount;

    /** 订单状态 */
    private Integer orderStatus;

    /** 支付状态 */
    private Integer payStatus;

    /** 支付方式 */
    private String payType;

    private LocalDateTime orderTime;
    private LocalDateTime payTime;

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderItemPO> items = new ArrayList<>();
}
