package com.example.demo.repository;

import com.example.demo.bean.po.Favorite;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    Favorite findByUserIdAndTargetIdAndType(Long userId, Long targetId, Integer type);

    List<Favorite> findAllByUserId(Long userId);

    List<Favorite> findAllByUserIdAndType(Long userId, Integer type);
}
