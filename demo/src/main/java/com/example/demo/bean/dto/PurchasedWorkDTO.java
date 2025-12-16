package com.example.demo.bean.dto;

import lombok.Data;

import java.util.List;

@Data
public class PurchasedWorkDTO {
    private Long artistWorkId;
    private String workName;

    private List<PurchasedVersionDTO> versions;
}
