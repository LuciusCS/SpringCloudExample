package com.example.demo.bean.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class WatermarkDTO {
    private Long id;
    private String style;
    private String name;
    private String backgroundFile;
    private String psdConfig;
    private String supportedSignatureLengths;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
