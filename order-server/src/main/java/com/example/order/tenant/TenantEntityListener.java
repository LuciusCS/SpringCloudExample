package com.example.order.tenant;

import jakarta.persistence.PrePersist;
import org.springframework.stereotype.Component;

@Component
public class TenantEntityListener {
    @PrePersist
    public void setTenantId(Object entity) {
        if (entity instanceof TenantSupport) {
            // 获取租户 ID
            Long tenantId = TenantContext.getTenantId();
            if (tenantId != null) {
                ((TenantSupport) entity).setTenantId(tenantId);
            }
        }
    }
}
