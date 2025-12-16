package com.example.demo.bean.dto;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDetailDTO {
    private String orderNo;
    private Integer orderStatus;
    private Integer payStatus;
    private BigDecimal payAmount;
    private LocalDateTime orderTime;

    private List<OrderItemDTO> items;
}
