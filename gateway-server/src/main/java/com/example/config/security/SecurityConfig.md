


✅ 实际微服务架构中权限控制的 

方式                          	说明	                    适用范围	            是否推荐
注解式（如 @PreAuthorize）	控制单个接口	                简单系统	            ✅ 小项目
网关统一鉴权          	在 API Gateway 层统一控制权限	       全局	            ✅✅✅ 推荐
权限中心中台服务          	专门的权限服务控制所有权限逻辑	    大型企业系统	        ✅✅✅ 推荐
动态权限加载	        权限配置在数据库，由代码或策略动态解析  多租户或变更频繁的系统	✅ 推荐



✅ 推荐方案 1：在 Spring Cloud Gateway 实现统一权限控制

[客户端]
↓ 请求 API
[Gateway 网关]
↓ 鉴权校验 Token、Scope、Role、Permission
↓ 鉴权通过
[下游服务]


可以对接口使用统一的前缀，根据前缀控制接口请求，比如：前缀是admin 只允许拥有Admin的用户请求，前缀时user，普通用户，admin用户都可以进行请求

✅ 示例代码：基于路径 + 权限控制的 Gateway 过滤器

```java
@Component
public class AuthorizationFilter implements GatewayFilter {

    private static final Map<String, List<String>> PERMISSION_MAPPING = Map.of(
        "/admin/**", List.of("ROLE_ADMIN"),
        "/user/**", List.of("ROLE_USER", "ROLE_ADMIN")
    );

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        String token = extractToken(exchange.getRequest());

        if (StringUtils.isBlank(token)) {
            return unauthorized(exchange);
        }

        Claims claims = JwtUtil.parseToken(token);
        List<String> roles = claims.get("roles", List.class);

        if (!hasPermission(path, roles)) {
            return forbidden(exchange);
        }

        return chain.filter(exchange);
    }

    private boolean hasPermission(String path, List<String> roles) {
        for (Map.Entry<String, List<String>> entry : PERMISSION_MAPPING.entrySet()) {
            if (PathMatcher.match(entry.getKey(), path)) {
                return roles.stream().anyMatch(entry.getValue()::contains);
            }
        }
        return true;
    }
}


```


✅ 推荐方案 2：引入统一权限服务（RBAC/ABAC）


如果权限规则较复杂（如部门维度、数据范围控制、多租户）：

👉 建议把权限管理抽象为一个独立微服务权限中心（例如 RBAC 系统）
权限中心功能：
模块                  	    说明
用户管理            	    用户 -> 角色
角色管理	                角色 -> 权限
权限定义	            URI、菜单、按钮、数据级权限等
权限校验 API	    下游服务/Gateway 调用接口校验是否有权限
然后：

下游服务调用权限中心进行鉴权
或 Gateway 调用权限中心进行统一过滤


✅ 推荐方案 3：基于数据库动态权限配置

如果你的系统要求：

权限可以通过后台界面配置
每个 URI 的权限规则随时调整
你可以将权限与 URI 匹配配置保存到数据库中：

uri                 | required_roles
--------------------|-------------------
/api/admin/**       | ROLE_ADMIN
/api/user/**        | ROLE_USER,ROLE_ADMIN
然后在 Gateway 或权限服务中动态加载并判断。


🚀 最佳实践组合

JWT 中包含：scope, roles, permissions
Gateway 统一处理：
    鉴权
    路由转发
    角色/权限过滤
各微服务内部：
    保留注解方式，兜底处理
权限定义 & 用户管理：
    提供权限管理后台
    权限数据落地 DB，结合 Redis 缓存做刷新