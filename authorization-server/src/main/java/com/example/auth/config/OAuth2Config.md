


将 OAuth2 授权服务器的配置和 Spring Security 的配置分开，可以更清晰地分离关注点。
AuthorizationServerConfig 专注于授权服务器的配置，而 SecurityConfig 专注于如何保护资源服务器和认证、授权等安全控制。



在 Spring Security 6.1 中，oauth2ResourceServer(oauth2 -> oauth2.jwt()) 的 jwt() 方法已被弃用，并且标记为即将移