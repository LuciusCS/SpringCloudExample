package com.example.demo.bean.dto;
import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class OrderItemDTO {
    private Long productId;
    private String productTitle;
    private String buyMode;
    private BigDecimal subtotalAmount;

    private List<OrderContentDTO> contents;
}
