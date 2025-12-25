package com.example.demo.bean.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class SalesRecordDTO {
    private Long id;
    private String orderNo;
    private LocalDateTime payTime;
    private BigDecimal amount;

    /** 仅商品销售记录有此字段 */
    private Integer quantity;

    /** 商品名称 或 作品名称 */
    private String name;
}
