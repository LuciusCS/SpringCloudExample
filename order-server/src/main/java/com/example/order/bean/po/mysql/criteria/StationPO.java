package com.example.order.bean.po.mysql.criteria;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "station")
public class StationPO {

    @Id
    private Long id;

    private String name;

    @Column(name = "area_id")
    private Long areaId;

}
