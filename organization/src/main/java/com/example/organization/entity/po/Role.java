package com.example.organization.entity.po;


import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Set;


///用于表示用户角色
@Data
@Entity
@Table(name = "role")
public class Role extends BasePo {
    private String code;
    private String name;
    private String description;

    ///在数据库中表不存在
//    @TableField(exist = false)
    private Set<String> resourceIds;
}
