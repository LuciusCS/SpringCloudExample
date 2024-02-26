package com.example.common.entity.po;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
@MappedSuperclass   ///注解用于指示实体类是一个超类（父类），它将被子类继承而不是单独映射到数据库表中
public class BasePo implements Serializable {

    public final static String DEFAULT_USERNAME = "system";


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String createdBy;


    private Date createdTime;

    private String updatedBy;

    private Date updatedTime;
}
