package com.example.order.bean;


import com.example.order.tenant.TenantEntityListener;
import com.example.order.tenant.TenantSupport;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

/**
 *
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(TenantEntityListener.class)
@Entity
@Table(name = "`order`")
public class Order implements Serializable, TenantSupport {


    @Serial
    private static final long serialVersionUID = -701119416679977321L;

    @Id
    private Long id;


    private String orderId;

    private String userId;

    private Long tenantId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @Override
    public   void setTenantId(Long tenantId) {
        this.tenantId=tenantId;
    }

    @Override
    public  Long getTenantId() {
        return  tenantId;
    }
}
