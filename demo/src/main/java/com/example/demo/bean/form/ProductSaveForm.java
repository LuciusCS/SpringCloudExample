package com.example.demo.bean.form;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class ProductSaveForm {
    private Long artistId;
    private String coverUrl;
    private String title;
    private String tags;
    private String themeColor;
    private  Integer type;
    private List<ArtistWorkForm> works;

    // 是否允许抱盒
    private Boolean allowBox;

    // 用户消费需满足的最低价格
    private BigDecimal minUserPrice;

    // 打包价格
    private BigDecimal packagePrice;

    // 单张价格
    private BigDecimal singlePrice;

    // 是否需要关注
    private Boolean needFollow;

    // 允许助力
    private Boolean allowHelp;

    // 商品包含张数
    private Integer quantity;

    // 优化价格（可理解为折扣价或促销价）
    private BigDecimal optimizedPrice;

    // 上架时间
    private LocalDateTime publishTime;
}
