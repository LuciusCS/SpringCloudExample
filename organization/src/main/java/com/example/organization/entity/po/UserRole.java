package com.example.organization.entity.po;
import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 用于表示用户角色
 */
@Data
@Entity
@Table(name = "user_role")
public class UserRole  extends BasePo {
    private String userId;
    private String roleId;
}
