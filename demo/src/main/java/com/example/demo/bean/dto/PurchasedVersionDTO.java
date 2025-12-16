package com.example.demo.bean.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PurchasedVersionDTO {
    private Long versionId;
    private String previewUrl;
    private String originalUrl;
    private LocalDateTime buyTime;
}
