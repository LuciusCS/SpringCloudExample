package com.example.order.bean;

import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 提现记录实体
 */
@Entity
@Table(name = "withdrawal_record")
@Data
public class WithdrawalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false, length = 64)
    private String openid;

    @Column(name = "real_name", nullable = false, length = 100)
    private String realName;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "batch_id", unique = true, length = 64)
    private String batchId;

    @Column(name = "out_batch_no", unique = true, length = 64)
    private String outBatchNo;

    @Column(name = "out_detail_no", unique = true, length = 64)
    private String outDetailNo;

    @Column(nullable = false, length = 20)
    private String status = "PENDING";

    @Column(length = 200)
    private String remark;

    @Column(name = "fail_reason", length = 500)
    private String failReason;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
