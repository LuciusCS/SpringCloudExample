package com.example.demo.bean.form;

import lombok.Data;

@Data
public class WatermarkSaveForm {
    private String style;
    private String name;
    private String backgroundFile;
    private String psdConfig;
    private String supportedSignatureLengths;
}
