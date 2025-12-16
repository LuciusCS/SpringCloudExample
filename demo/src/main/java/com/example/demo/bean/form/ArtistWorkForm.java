package com.example.demo.bean.form;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtistWorkForm {
    private String name;
    private Integer workType;   // IMAGE / PSD
    private String tags;
    private BigDecimal transparency;
    private Integer saleType;   // GIFT / SELL

    private List<ArtistWorkVersionForm> versions;
}
