package com.example.demo.repository;

import com.example.demo.bean.po.UserQQBindPO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserQQBindRepository extends JpaRepository<UserQQBindPO, Long> {
    Optional<UserQQBindPO> findByUserId(Long userId);

    Optional<UserQQBindPO> findByQq(String qq);
}
