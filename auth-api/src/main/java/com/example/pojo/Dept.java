package com.example.pojo;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

//所有的实体类都需要序列化，要不然传输的过程中会出错

@Data
@NoArgsConstructor
@Accessors(chain = true )  ///链式写法
public class Dept implements Serializable {  //dept 实体类， orm 类表关系映射  需要添加一个base类，其中包括创建日期和创建人等其他功能

    private  int depoNum;  //主键

    //表示存在哪个数据库的字段， 一个微服务对应一个数据库，同一个信息可能存在不同的数据库
    private String dbSource;

    public Dept(int depoNum, String dbSource) {

        this.dbSource = dbSource;
    }
}
