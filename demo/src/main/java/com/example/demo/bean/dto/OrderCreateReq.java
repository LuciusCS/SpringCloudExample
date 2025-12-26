package com.example.demo.bean.dto;

import lombok.Data;

@Data
public class OrderCreateReq {

    private Long productId;

    /** SINGLE / BOX */
    private String buyMode;

    /** 抱盒数量（BOX 时必填） */
    private Integer boxCount;

    /** 单买选中的作品ID列表 (SINGLE 时必填) */
    private java.util.List<Long> workIds;
}
