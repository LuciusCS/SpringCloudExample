package com.example.demo.repository;

import com.example.demo.bean.po.WorkSalesRecordPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface WorkSalesRecordRepository extends JpaRepository<WorkSalesRecordPO, Long> {

    Page<WorkSalesRecordPO> findByArtistWorkIdOrderByPayTimeDesc(Long artistWorkId, Pageable pageable);

    @Query("SELECT SUM(w.amount) FROM WorkSalesRecordPO w WHERE w.artistWorkId = :artistWorkId")
    BigDecimal sumAmountByArtistWorkId(@Param("artistWorkId") Long artistWorkId);
}
