package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "favorite")
@Data
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long targetId;

    /**
     * 0: Direct Purchase (Product), 1: Gacha (Product)
     * Although both are products, preserving type might be useful if target tables
     * differ later.
     * Currently both map to ProductPO if logic is consistent.
     */
    private Integer type;

    private LocalDateTime createTime;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
    }
}
