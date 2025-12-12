package com.example.order.repository;

import com.example.order.bean.WithdrawalRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface WithdrawalRecordRepository extends JpaRepository<WithdrawalRecord, Long> {

    Optional<WithdrawalRecord> findByBatchId(String batchId);

    Optional<WithdrawalRecord> findByOutBatchNo(String outBatchNo);

    List<WithdrawalRecord> findByUserIdOrderByCreatedAtDesc(Long userId);

    List<WithdrawalRecord> findByStatus(String status);
}
