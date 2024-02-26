package com.example.organization.entity.po;

import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 *
 * 用于表示组权限
 * 组权限是多对多
 */

@Data
@Entity
@Table(name = "group_permission")
public class GroupPermission extends BasePo {

    private  String groupId;
    private String permissionId;

}
