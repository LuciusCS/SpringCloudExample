package com.example.demo.repository;

import com.example.demo.bean.po.StorePO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<StorePO, Long> {
    Optional<StorePO> findByUserId(Long userId);
}
