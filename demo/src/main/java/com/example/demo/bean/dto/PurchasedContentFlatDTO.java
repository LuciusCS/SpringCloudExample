package com.example.demo.bean.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PurchasedContentFlatDTO {
    private Long productId;
    private String productTitle;

    private Long artistWorkId;
    private String workName;

    private Long versionId;
    private String previewUrl;
    private String originalUrl;

    private Integer productType;

    private LocalDateTime buyTime;
}
