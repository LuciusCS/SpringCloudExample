package com.example.demo.bean.form;

import lombok.Data;

@Data
public class ArtistWorkVersionUpdateForm {

    private Long id;  // 有值表示更新，无值表示新增
    private String previewUrl;
    private String originalUrl;
    private String silhouetteUrl;
}
