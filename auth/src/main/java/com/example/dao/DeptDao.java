package com.example.dao;

import com.example.pojo.Dept;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

@Mapper
@Repository
public interface DeptDao {


    public boolean addDept(Dept dept);


}
