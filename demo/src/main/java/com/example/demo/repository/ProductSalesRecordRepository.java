package com.example.demo.repository;

import com.example.demo.bean.po.ProductSalesRecordPO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;

public interface ProductSalesRecordRepository extends JpaRepository<ProductSalesRecordPO, Long> {

    Page<ProductSalesRecordPO> findByProductIdOrderByPayTimeDesc(Long productId, Pageable pageable);

    @Query("SELECT SUM(p.amount) FROM ProductSalesRecordPO p WHERE p.productId = :productId")
    BigDecimal sumAmountByProductId(@Param("productId") Long productId);

    @Query("SELECT SUM(p.quantity) FROM ProductSalesRecordPO p WHERE p.productId = :productId")
    Integer sumQuantityByProductId(@Param("productId") Long productId);
}
