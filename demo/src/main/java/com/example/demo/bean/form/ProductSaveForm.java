package com.example.demo.bean.form;

import lombok.Data;

import java.util.List;

@Data
public class ProductSaveForm {
    private Long artistId;
    private String coverUrl;
    private String title;
    private String tags;
    private String themeColor;

    private List<ArtistWorkForm> works;
}
