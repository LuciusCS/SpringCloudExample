package com.example.demo.bean.form;


import lombok.Data;

@Data
public class ProductUpdateForm {

    private Long id;  // 必填，指定更新的商品
    private Long artistId;
    private String coverUrl;
    private String title;
    private String tags;
    private String themeColor;

    private List<ArtistWorkUpdateForm> works;
}
