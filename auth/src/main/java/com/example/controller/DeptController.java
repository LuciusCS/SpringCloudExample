package com.example.controller;


import com.example.pojo.Dept;
import com.example.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


///提供restful服务

@RestController
public class DeptController {

    @Autowired
    private DeptService deptService;

//
    @Value("${pattern.dateformat}")   //这里获取的是nacos的配置信息
    private String dateFormat;
    @Value("${pattern.test}")   //这里获取的是nacos的配置信息
    private String test;


    @PostMapping("dept/add")
    public boolean addDept()
    {
        //
        System.out.println(dateFormat);
        System.out.println(test);
         return  deptService.addDept(new Dept(100,"123"));


//        return  true;
    }




}
