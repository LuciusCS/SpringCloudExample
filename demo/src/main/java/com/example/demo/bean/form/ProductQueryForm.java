package com.example.demo.bean.form;

import lombok.Data;

@Data
public class ProductQueryForm {
    private Long artistId;
    private String tags; // 国风,插画
    private String themeColor;
    private String title;
    private Integer type;

}
