package com.example.controller;


import com.example.pojo.Dept;
import com.example.service.DeptService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;


///提供restful服务

@RestController
@RefreshScope    //用于从nacos中获取的配置信息动态刷新，要不然nacos配置信息publish后程序不刷新
public class DeptController {

    @Autowired
    private DeptService deptService;

//
    @Value("${pattern.dateformat}")   //这里获取的是nacos的配置信息,使用的是auth-dev.yaml
    private String dateFormat;
    @Value("${pattern.test}")   //这里获取的是nacos的配置信息,使用的是auth-dev.yaml
    private String test;
    //
    @Value("${pattern1.dateformat1}")   //这里获取的是nacos，使用的是  auth.yaml
    private String dateFormat1;
    @Value("${pattern1.test1}")   //这里获取的是nacos的配置信息,使用的是auth.yaml
    private String test1;

    @Value("${allservice.config_string}")   //这里获取的是nacos，使用的是  auth.yaml
    private String sharedInfo1;



    @PostMapping("dept/add")
    public boolean addDept()
    {
        //
        System.out.println(dateFormat);
        System.out.println(dateFormat1);
        System.out.println(test);
        System.out.println(test1);
        System.out.println(sharedInfo1);
         return  deptService.addDept(new Dept(100,"123"));


//        return  true;
    }




}
