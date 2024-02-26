package com.example.organization.entity.po;

import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;


/**
 * 用于表示用户所在的组或部门
 */
@Data
@Entity
@Table(name = "user_group")
public class UserGroup  extends BasePo {
    private String userId;
    private String groupId;


}
