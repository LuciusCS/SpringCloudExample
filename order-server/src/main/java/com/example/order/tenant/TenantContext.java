package com.example.order.tenant;


import com.alibaba.ttl.TransmittableThreadLocal;

public class TenantContext {
    /**
     * 当前租户编号
     */
    private static final ThreadLocal<Long> TENANT_ID = new TransmittableThreadLocal<>();

    /**
     * 是否忽略租户
     */
    private static final ThreadLocal<Boolean> IGNORE = new TransmittableThreadLocal<>();

    /**
     * 获得租户编号
     *
     * @return 租户编号
     */
    public static Long getTenantId() {
        return TENANT_ID.get();
    }

    /**
     * 获得租户编号。如果不存在，则抛出 NullPointerException 异常
     *
     * @return 租户编号
     */
    public static Long getRequiredTenantId() {
        Long tenantId = getTenantId();
        if (tenantId == null) {
            throw new NullPointerException("TenantContextHolder 不存在租户编号！");
//                    + DocumentEnum.TENANT.getUrl());
        }
        return tenantId;
    }

    public static void setTenantId(Long tenantId) {
        TENANT_ID.set(tenantId);
    }

    public static void setIgnore(Boolean ignore) {
        IGNORE.set(ignore);
    }

    /**
     * 当前是否忽略租户
     *
     * @return 是否忽略
     */
    public static boolean isIgnore() {
        return Boolean.TRUE.equals(IGNORE.get());
    }

    public static void clear() {
        TENANT_ID.remove();
        IGNORE.remove();
    }
}

//public class TenantContext {
//    // 线程局部变量，确保每个线程有独立的租户 ID
//    private static final ThreadLocal<Long> tenantContext = new ThreadLocal<>();
//
//    // 设置租户 ID
//    public static void setTenantId(Long tenantId) {
//        tenantContext.set(tenantId);
//    }
//
//    // 获取租户 ID
//    public static Long getTenantId() {
//        return tenantContext.get();
//    }
//
//    // 清理租户上下文（用于线程池任务执行完后清理）
//    public static void clear() {
//        tenantContext.remove();
//    }
//}
