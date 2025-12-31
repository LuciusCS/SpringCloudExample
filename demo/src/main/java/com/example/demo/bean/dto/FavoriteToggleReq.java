package com.example.demo.bean.dto;

import lombok.Data;

@Data
public class FavoriteToggleReq {
    private Long targetId;
    /**
     * 0: Direct Purchase (Product), 1: Gacha (Product)
     */
    private Integer type;
}
