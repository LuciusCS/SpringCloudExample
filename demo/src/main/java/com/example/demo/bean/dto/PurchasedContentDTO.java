package com.example.demo.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchasedContentDTO {
    private Long productId;
    private String productTitle;

    private List<PurchasedWorkDTO> works;
}
