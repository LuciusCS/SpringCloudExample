package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_content", uniqueConstraints = @UniqueConstraint(columnNames = { "order_item_id", "artist_work_id",
        "content_type" }))
@Data
public class OrderContentPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long artistWorkId;
    private String workName;

    private Long artistWorkVersionId;

    private String previewUrl;
    private String originalUrl;
    private String silhouetteUrl;

    /** SALE / GIFT */
    private String contentType;

    private Boolean gift;
    private BigDecimal price;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_item_id", insertable = false, updatable = false)
    private OrderItemPO orderItem;

    @Column(name = "order_item_id")
    private Long orderItemId;
}
