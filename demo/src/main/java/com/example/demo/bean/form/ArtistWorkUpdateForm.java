package com.example.demo.bean.form;


import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

@Data
public class ArtistWorkUpdateForm {

    private Long id;  // 有值表示更新，无值表示新增
    private String name;
    private Integer workType;
    private String tags;
    private BigDecimal transparency;
    private Integer saleType;

    private List<ArtistWorkVersionUpdateForm> versions;
}
