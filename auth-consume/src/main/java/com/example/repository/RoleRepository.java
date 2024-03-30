package com.example.repository;


import com.example.entity.AuthRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<AuthRole,Long> {
    AuthRole findByName(String name);

}
