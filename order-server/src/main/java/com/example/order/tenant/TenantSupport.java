package com.example.order.tenant;

/**
 * 租户支持接口
 */
public interface TenantSupport {
    void setTenantId(Long tenantId);
    Long getTenantId();
}