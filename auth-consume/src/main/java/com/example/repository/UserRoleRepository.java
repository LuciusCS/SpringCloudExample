package com.example.repository;


import com.example.entity.AuthUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRoleRepository extends JpaRepository<AuthUserRole,Long> {

    List<AuthUserRole>findAllByUserId(Long id);
}
