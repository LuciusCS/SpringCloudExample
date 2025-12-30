package com.example.demo.bean.dto;

import lombok.Data;

@Data
public class MerchantAuditSubmitReq {
    private Long userId;
    private String merchantName;
    private String proofImages; // Comma separated URLs or JSON string
    private String qqContact;
}
