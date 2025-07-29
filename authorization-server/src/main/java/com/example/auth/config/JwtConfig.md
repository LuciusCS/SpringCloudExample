

请求通过 Bearer Token 携带 access_token 报错：

An error occurred while attempting to decode the Jwt: Signed JWT rejected: Invalid signature


错误原因是没有保存JWKSet

在JWTConfig中添加,下面的代码即可解决，同时将jwtset保存到redis中
```
 @Bean 
    @SneakyThrows
    public JWKSource<SecurityContext> jwkSource() {
    }
```




## Spring Security. OAuth2 为什么不需要配置jwt encode

在Spring Security OAuth2中，无需显式配置JWT编码器（JwtEncoder） 的原因在于框架提供了默认的自动配置机制。以下是详细解释：

1、自动生成密钥对
Spring Security 自带了 OAuth2TokenGenerator<OAuth2AccessToken>，用于自动生成 JWT。

当你使用 OAuth2AuthorizationServerConfiguration 配置授权服务器时，它已经默认提供了 JWT 生成的机制，不需要手动指定 JwtEncoder。

默认的JwtEncoder和JwtDecoder
当检测到类路径中存在JWT库（如nimbus-jose-jwt）时，框架会自动创建 NimbusJwtEncoder 和 NimbusJwtDecoder，并绑定到生成的密钥对

2、JWKSource 代替了 JwtEncoder

在配置代码中，已经提供了 JWKSource，而 JWKSource 本质上就是 JWT 密钥管理器，它直接供 Spring Security 使用，避免了手动创建 JwtEncoder 的需求。


## 默认行为


特性	                默认行为	                                        生产环境建议
密钥管理          自动生成RSA密钥对	                                 显式配置固定密钥对
JWT编码器/解码器	自动创建NimbusJwtEncoder/NimbusJwtDecoder	无需修改，除非更换算法或密钥类型
令牌声明	            包含基本声明（sub, iss, exp等）          	通过OAuth2TokenCustomizer扩展


总结

Spring Security OAuth2 服务器自动管理 JWT，不需要手动配置 JwtEncoder。
JWKSource 负责管理 JWT 密钥，Spring Security 自动使用它生成 JWT。
如果要手动生成 JWT，才需要 JwtEncoder，但通常不需要这样做。


## 在 JwtConfig 中可以设置过期时间

## JWT 中的 sub 和 aud

```
{
  "sub": "username",
  "aud": "client1234",
  "nbf": 1753089654,
  "scope": [
    "openid"
  ],
  "roles": "test",
  "iss": "http://localhost:8003",
  "exp": 1753089954,
  "iat": 1753089654,
  "jti": "d38fa33a-ee57-46d6-b627-dbd575c044d3"
}

```

   sub（Subject）：通常代表的是 授权的主体，即 用户。它通常是用户的唯一标识符（如用户名、用户 ID 或其他标识符）。
   在你提供的令牌中，sub 被设置为 "username"，意味着令牌是颁发给名为 "username" 的用户。
   aud（Audience）：这个字段通常表示令牌的目标受众。在 OAuth2 中，aud 字段通常表示客户端 ID（如 client1234），  
   它表示令牌是由哪个客户端应用请求的。在你提供的令牌中，aud 被设置为 "client1234"，意味着该令牌是为 client1234 客户端申请的。