package com.example.organization.entity.po;

import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


/**
 * 用于表示资源
 */
@Data
@Entity
@Table(name = "resource")
public class Resource  extends BasePo {
    private String code;
    private String type;
    private String url;
    private String method;
    private String name;
    private String description;
}
