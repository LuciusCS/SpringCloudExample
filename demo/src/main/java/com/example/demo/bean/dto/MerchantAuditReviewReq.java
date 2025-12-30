package com.example.demo.bean.dto;

import lombok.Data;

@Data
public class MerchantAuditReviewReq {
    private Long id;
    private Boolean pass; // true = approve, false = reject
    private String rejectReason;
}
