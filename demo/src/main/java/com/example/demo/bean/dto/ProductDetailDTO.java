package com.example.demo.bean.dto;


import lombok.Data;

import java.util.List;

@Data
public class ProductDetailDTO {
    private Long id;
    private Long artistId;
    private String coverUrl;
    private String title;
    private String tags;
    private String themeColor;

    private List<ArtistWorkDTO> works;
}
