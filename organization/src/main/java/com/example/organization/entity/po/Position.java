package com.example.organization.entity.po;

import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * 用于表示职务
 */
@Data
@Entity
@Table(name = "position")
public class Position  extends BasePo {
    private String name;
    private String description;
//    @TableLogic ///用于进行逻辑删除
    private String deleted = "N";
}
