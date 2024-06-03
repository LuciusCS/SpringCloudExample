package com.example.controller;

import com.example.entity.dto.EntityResponseBody;
import com.example.service.RoleService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.function.EntityResponse;

@RestController
@RequestMapping("admin")
public class AdminAccessController {

    @Autowired
    UserService userService;

    @Autowired
    RoleService roleService;

    @GetMapping("user-list")
    public ResponseEntity<Object>getAllUserList(){
        return EntityResponseBody.generateResponse("管理员获取用户列表", HttpStatus.OK,userService.retriveAllUserList());
    }


    @GetMapping("role-list")
    public ResponseEntity<Object>getAllRoleList(){
        return EntityResponseBody.generateResponse("管理员获取角色列表" ,HttpStatus.OK,roleService.findAllRole());
    }

}
