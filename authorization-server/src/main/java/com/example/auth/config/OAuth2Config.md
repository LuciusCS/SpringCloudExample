


将 OAuth2 授权服务器的配置和 Spring Security 的配置分开，可以更清晰地分离关注点。
AuthorizationServerConfig 专注于授权服务器的配置，而 SecurityConfig 专注于如何保护资源服务器和认证、授权等安全控制。



在 Spring Security 6.1 中，oauth2ResourceServer(oauth2 -> oauth2.jwt()) 的 jwt() 方法已被弃用，并且标记为即将移




OpenID Connect 使用一个特殊的权限范围值 openid 来控制对 UserInfo 端点的访问。
OpenID Connect 定义了一组标准化的 OAuth 权限范围，对应于用户属性的子集profile、email、 phone、address，参见表格：


权限范围                     声明
openid                      sub
profile            Name、family_name、given_name、middle_name、nickname、preferred_username、profile、 picture、website、gender、birthdate、zoneinfo、locale、updated_at
email                   email、email_verified
address                 address,是一个 JSON 对象、包含 formatted、street_address、locality、region、postal_code、country
phone                   phone_number、phone_number_verified
