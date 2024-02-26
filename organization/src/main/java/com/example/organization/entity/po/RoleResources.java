package com.example.organization.entity.po;

import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 角色和资源是多对多的关系
 */
@Data
@Entity
@Table(name = "role_resource")
public class RoleResources  extends BasePo {
    private String roleId;
    private String resourceId;
}

