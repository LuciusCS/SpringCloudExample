package com.example.demo.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchasedContentDTO {
    private Long productId;
    private String productTitle;
    private Integer productType;
    private String storeLogo;

    private List<PurchasedWorkDTO> works;
}
