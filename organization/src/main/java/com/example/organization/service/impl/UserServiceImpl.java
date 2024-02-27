package com.example.organization.service.impl;

import com.example.organization.dao.UserDao;
import com.example.organization.entity.po.User;
import com.example.organization.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service  ///MVC分层中，标识服务层
public class UserServiceImpl implements IUserService {

    @Autowired
    UserDao userDao;

    @Override
    public User getUser(long id) {

//        userDao.findById(id) 返回的是一个optional类型的数据

        return userDao.findById(id).orElse(null);
    }
}
