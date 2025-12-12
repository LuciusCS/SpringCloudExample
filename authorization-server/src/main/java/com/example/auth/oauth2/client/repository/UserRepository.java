package com.example.auth.oauth2.client.repository;

import com.example.auth.bean.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByWechatOpenid(String wechatOpenid);
}
