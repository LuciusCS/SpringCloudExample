package com.example.demo.repository;

import com.example.demo.bean.po.MerchantAuditPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MerchantAuditRepository extends JpaRepository<MerchantAuditPO, Long> {
    List<MerchantAuditPO> findByUserId(Long userId);

    List<MerchantAuditPO> findByStatus(Integer status);
}
