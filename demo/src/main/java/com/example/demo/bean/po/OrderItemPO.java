package com.example.demo.bean.po;
import jakarta.persistence.*;
import lombok.Data;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "order_item")
@Data
public class OrderItemPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id")
    private Long orderId;

    /** 商品ID */
    private Long productId;

    /** 商品快照 */
    private String productTitle;
    private String coverUrl;

    /** 商品规则快照 */
    private Boolean allowBox;
    private Boolean allowHelp;
    private Boolean needFollow;

    /** 商品类型 */
    private Integer productType;

    /** 购买数量（张数 / 套数） */
    private Integer buyQuantity;

    /** 成交单价 */
    private BigDecimal dealPrice;

    /** 小计金额 */
    private BigDecimal subtotalAmount;

    /** 购买方式：SINGLE / BOX */
    private String buyMode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", insertable = false, updatable = false)
    private OrderPO order;

    @OneToMany(
            mappedBy = "orderItem",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<OrderContentPO> contents = new ArrayList<>();
}
