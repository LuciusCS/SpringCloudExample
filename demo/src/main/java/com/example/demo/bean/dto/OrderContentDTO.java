package com.example.demo.bean.dto;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderContentDTO {
    private Long artistWorkId;
    private String workName;
    private Long versionId;
    private String previewUrl;

    /** SALE / GIFT */
    private String contentType;

    private BigDecimal price;
}
