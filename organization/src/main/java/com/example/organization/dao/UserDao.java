package com.example.organization.dao;

import com.example.organization.entity.po.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository  ///MVC分层中，标识持久层
public interface UserDao extends JpaRepository<User,Long> {
}
