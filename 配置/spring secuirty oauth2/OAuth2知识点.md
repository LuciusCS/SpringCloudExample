


# scop role permission 之间的区别

## scope 和 role/permission 各自有不同的使用场景：
scope 主要用于 OAuth2 授权流程中，控制客户端应用程序的权限。
role/permission 更侧重于对 用户 的权限管理，适用于控制用户可以执行哪些操作。
在微服务架构中，scope 通常用于控制 客户端 对服务的访问，而 role/permission 用于控制 用户 的操作权限。
你可以在微服务接口中独立配置 scope，或者在 API Gateway 层统一进行 scope 配置。



# 客户端模式（Client Credentials）和密码模式（Password）在权限控制

## 客户端模式（Client Credentials）
   核心用途：服务端与服务端之间的认证（如微服务间调用），不涉及用户身份。
权限控制方式：
必须使用 scope：客户端自身的权限通过 scope 声明（在客户端注册时配置）。
无需角色/权限：因为该模式没有用户上下文，无法使用基于用户的 ROLE 或 Permission。


## 密码模式（Password）
   核心用途：用户通过客户端提交用户名/密码换取令牌（适用于受信任的客户端）。
权限控制方式：
优先使用 ROLE 和 Permission：权限基于用户的身份（从 UserDetailsService 加载）。
可选使用 scope：可额外结合 scope 限制客户端权限（但用户权限是核心）。


模式	          权限控制核心	是否用 scope	      是否用 ROLE/Permission
客户端模式	  客户端自身权限	  必须使用	       不支持（无用户上下文）
密码模式       用户身份权限	可选（额外限制）   	必须使用