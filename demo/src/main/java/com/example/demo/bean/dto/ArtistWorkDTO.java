package com.example.demo.bean.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtistWorkDTO {
    private Long id;
    private String name;
    private Integer workType;
    private String tags;
    private BigDecimal transparency;
    private Integer saleType;
    private List<ArtistWorkVersionDTO> versions;
}
