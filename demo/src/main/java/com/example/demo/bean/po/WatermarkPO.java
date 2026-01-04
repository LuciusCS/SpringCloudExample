package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "watermark")
public class WatermarkPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 风格：古风、可爱、酷帅、情绪等
    @Column(nullable = false)
    private String style;

    // 款式名称：晴天、幸福、夏日祭等
    @Column(nullable = false)
    private String name;

    // background.png 文件名
    @Column(name = "background_file")
    private String backgroundFile;

    // psd_config 文件名
    @Column(name = "psd_config")
    private String psdConfig;

    // 署字长度 1,2,4 等
    @Column(name = "supported_signature_lengths")
    private String supportedSignatureLengths;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    @Column(name = "update_time")
    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        if (createTime == null) {
            createTime = LocalDateTime.now();
        }
        updateTime = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updateTime = LocalDateTime.now();
    }
}
