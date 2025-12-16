package com.example.demo.repository;

import com.example.demo.bean.po.ArtistWorkPO;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ArtistWorkRepository  extends JpaRepository<ArtistWorkPO, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select w from ArtistWorkPO w where w.product.id = :productId")
    List<ArtistWorkPO> findByProductIdForUpdate(Long productId);
}
