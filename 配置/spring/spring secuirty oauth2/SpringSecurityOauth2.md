# Spring Authorization Server (SAS)

> [!IMPORTANT]
> 旧的 `spring-security-oauth2` 项目已于 2022 年停止维护。
> 现在 Spring 官方推荐的授权服务器解决方案是 **Spring Authorization Server (SAS)**。

## 1. 简介
Spring Authorization Server 是一个基于 Spring Security 构建的框架，用于构建符合 OAuth 2.1 和 OpenID Connect 1.0 标准的授权服务器。

## 2. 核心组件

### RegisteredClientRepository
管理注册的客户端（Client）。类似于 Spring Security 中的 `UserDetailsService`，但它是针对客户端的。
可以基于内存（`InMemoryRegisteredClientRepository`）或数据库（`JdbcRegisteredClientRepository`）实现。

```java
@Bean
public RegisteredClientRepository registeredClientRepository() {
    RegisteredClient registeredClient = RegisteredClient.withId(UUID.randomUUID().toString())
            .clientId("messaging-client")
            .clientSecret("{noop}secret")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
            .authorizationGrantType(AuthorizationGrantType.CLIENT_CREDENTIALS)
            .redirectUri("http://127.0.0.1:8080/login/oauth2/code/messaging-client-oidc")
            .scope(OidcScopes.OPENID)
            .scope("message.read")
            .scope("message.write")
            .clientSettings(ClientSettings.builder().requireAuthorizationConsent(true).build())
            .build();

    return new InMemoryRegisteredClientRepository(registeredClient);
}
```

### AuthorizationServerSettings
配置授权服务器的设置，例如 Issuer URL、端点路径等。

```java
@Bean
public AuthorizationServerSettings authorizationServerSettings() {
    return AuthorizationServerSettings.builder().build();
}
```

### TokenSettings
配置令牌的有效期、是否复用 Refresh Token 等策略。

## 3. 迁移指南
如果你正在使用旧的 `spring-security-oauth2`，建议尽快迁移。
主要区别：
- **依赖变更**：使用 `spring-boot-starter-oauth2-authorization-server`。
- **配置方式**：不再使用 `@EnableAuthorizationServer`，而是通过 Security Filter Chain 进行配置。
- **协议支持**：SAS 默认支持 OAuth 2.1，对安全性有更高要求（如 PKCE 强制开启）。

## 4. 参考资料
- [官方文档](https://docs.spring.io/spring-authorization-server/reference/)
- [官方示例](https://github.com/spring-projects/spring-authorization-server/tree/main/samples)