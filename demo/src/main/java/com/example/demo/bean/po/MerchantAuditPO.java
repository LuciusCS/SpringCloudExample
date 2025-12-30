package com.example.demo.bean.po;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "merchant_audit")
@Data
public class MerchantAuditPO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private String merchantName;

    /**
     * Comma separated image URLs
     */
    @Column(columnDefinition = "TEXT")
    private String proofImages;

    private String qqContact;

    /**
     * 0: PENDING, 1: APPROVED, 2: REJECTED
     */
    private Integer status;

    private String rejectReason;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    @PrePersist
    public void prePersist() {
        this.createTime = LocalDateTime.now();
        this.updateTime = LocalDateTime.now();
        if (this.status == null) {
            this.status = 0; // Default Pending
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updateTime = LocalDateTime.now();
    }
}
