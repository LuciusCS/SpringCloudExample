package com.example.demo.bean.dto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class OrderCreateResp {

    private String orderNo;
    private BigDecimal payAmount;
}
