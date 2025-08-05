package com.example.order.bean.po.mysql.criteria;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "operator")
public class OperatorPO
{

    @Id
    private Long id;

    private String name;
}
