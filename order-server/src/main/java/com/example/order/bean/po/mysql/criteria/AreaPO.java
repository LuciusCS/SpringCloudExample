package com.example.order.bean.po.mysql.criteria;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "area")
public class AreaPO {

    @Id
    private Long id;

    private String name;

    @Column(name = "operator_id")
    private Long operatorId;


}
