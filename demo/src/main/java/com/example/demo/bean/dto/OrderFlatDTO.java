package com.example.demo.bean.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class OrderFlatDTO {


    // order
    private String orderNo;
    private Integer orderStatus;
    private Integer payStatus;
    private BigDecimal payAmount;
    private LocalDateTime orderTime;

    // item
    private Long productId;
    private String productTitle;
    private String buyMode;
    private BigDecimal subtotalAmount;

    // content
    private Long artistWorkId;
    private String workName;
    private Long versionId;
    private String previewUrl;
    private String contentType;
    private BigDecimal price;
}
