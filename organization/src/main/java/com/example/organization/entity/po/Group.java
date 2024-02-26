package com.example.organization.entity.po;

import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


/***
 * 用于表示组或部门
 * 是分层级的
 *
 */
@Data
@Entity
@Table(name = "group")
public class Group extends BasePo {

    private String name;

    ///组织机构是进行分层的
    private String parentId;
    private String description;

    private String deleted = "N";
}
