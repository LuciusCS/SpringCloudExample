package com.example.demo.bean.dto;

import lombok.Data;

import java.util.List;


@Data
public class ProductDTO {

    private Long id;
    private Long artistId;
    private String coverUrl;
    private String title;
    private String tags;
    private String themeColor;
    private  Integer type;
    private List<ArtistWorkDTO> works;
}
