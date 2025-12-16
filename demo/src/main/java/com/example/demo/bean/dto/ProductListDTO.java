package com.example.demo.bean.dto;


import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ProductListDTO {

    private Long id;
    private Long artistId;
    private String coverUrl;
    private String title;
    private String tags;
    private String themeColor;
    private LocalDateTime createTime;
}
