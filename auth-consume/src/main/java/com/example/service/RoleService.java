package com.example.service;


import com.example.entity.AuthRole;
import com.example.entity.AuthUser;
import com.example.repository.RoleRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class RoleService {


    @Autowired
    private RoleRepository roleRepository;

    public AuthRole save(AuthRole role){
        return roleRepository.save(role);
    }


    public List<AuthRole> findAllRole(){
        return  roleRepository.findAll();
    }

    public  AuthRole findDefaultRole(){
        return  findAllRole().stream().findFirst().orElse(null);
    }

    public AuthRole findRoleByName(String role){
        return  findAllRole().stream().filter(r->r.getName().equals(role)).findFirst().orElse(null);
    }

}
