package com.example.repository;


import com.example.entity.AuthUser;
import com.example.entity.AuthUserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<AuthUser,Long> {
    AuthUser findByUserNameAndPassword(String userName,String password);

    AuthUser findByUserName(String username);
}
