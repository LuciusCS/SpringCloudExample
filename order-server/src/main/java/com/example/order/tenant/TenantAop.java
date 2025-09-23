package com.example.order.tenant;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.stereotype.Component;

/**
 * @Aspect 注解的类在 Spring 启动时会被自动代理和处理，通常在启动时，
 * Spring 会扫描到这个切面并创建代理对象，因此会有一次调用 addTenantFilter 的情况。这个行为通常与 AOP 的切面织入过程相关，
 */
@Aspect
@Component
public class TenantAop {

    /**
     * 用于查询操作，添加查询条件
     * @param pjp
     * @return
     * @throws Throwable
     */
    @Around("execution(* com.example.order.tenant..*(..)) && !@annotation(com.example.order.tenant.IgnoreTenant)")
    public Object addTenantFilter(ProceedingJoinPoint pjp) throws Throwable {
        Long tenantId = TenantContext.getTenantId();
        if (tenantId != null) {
            // 获取当前查询方法的参数，并为查询条件动态添加租户 ID
//            Object[] args = pjp.getArgs();
            // 这里可以根据实际情况构造租户 ID 条件
            // 例如：修改查询的 `where` 条件，或者通过修改 SQL 加上 `tenant_id` 条件
            // 获取当前查询方法的参数
            Object[] args = pjp.getArgs();

            // 假设查询方法的第一个参数是查询条件（例如：Specification 或 Criteria）
//            if (args.length > 0 && args[0] instanceof Criteria) {
//                Criteria criteria = (Criteria) args[0];
//                // 在原始查询条件基础上添加租户 ID 条件
//                criteria.add(Restrictions.eq("tenantId", tenantId));
//            }
            /// 这里没有看明白，而且这里的代码有问题，不能正确执行
        }
        return pjp.proceed(); // 执行原方法
    }
}