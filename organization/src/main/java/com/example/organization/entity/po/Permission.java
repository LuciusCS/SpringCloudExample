package com.example.organization.entity.po;

import com.example.common.entity.po.BasePo;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.Date;

/**
 * 用于表示数据资源权限
 */
@Data
@Entity
@Table(name = "permission")
public class Permission extends BasePo {
    /**
     * 资源类型：hive，hdfs
     */
    private String resType;

    /**
     * 资源地区
     */
    private String area;

    /**
     * 资源完整路径
     */
    private String resFullPath;

    /**
     * 资源全名
     */
    private String resFullName;

    /**
     * 资源操作位：读，写，查询
     */
    private String operationBit;

    /**
     * 过期日期
     */
    private Date expireDate;

//    @TableLogic(value = "N",delval = "Y")
    private String deleted = "N";
}
