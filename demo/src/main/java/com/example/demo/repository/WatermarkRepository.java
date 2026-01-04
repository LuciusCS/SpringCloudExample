package com.example.demo.repository;

import com.example.demo.bean.po.WatermarkPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

@Repository
public interface WatermarkRepository extends JpaRepository<WatermarkPO, Long>, JpaSpecificationExecutor<WatermarkPO> {
}
