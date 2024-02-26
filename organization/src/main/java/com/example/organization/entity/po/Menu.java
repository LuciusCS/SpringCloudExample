package com.example.organization.entity.po;


import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

///用于表示用户菜单
@Data
@Entity
@Table(name = "menu")
public class Menu extends BasePo {
    private String parentId;
    private String name;
    private String type;
    private String href;
    private String icon;
    private int orderNum;
    private String description;
}

