

## 一、 Postman 测试步骤
### 1. 授权码模式
步骤 1：获取授权码（Code）
构造授权 URL：

```
Copy
    GET http://{auth-server}/oauth/authorize?client_id={clientId}&response_type=code&redirect_uri={callbackUrl}&scope={scopes}

```

示例：http://localhost:8004/oauth2/authorize?response_type=code&client_id=client1234&redirect_uri=http://localhost:8004/callback

手动访问 URL：在浏览器中打开该链接，登录并授权后，浏览器会重定向到 redirect_uri，URL 中会包含 code（如 http://localhost:8080/callback?code=abc123）。

步骤 2：用 Code 换取 Access Token
Postman 配置：

请求方法：POST 到 http://{auth-server}/oauth/token。

```
Authorization 选项卡：选择 Basic Auth，输入 client_id 和 client_secret。

```

Body 选项卡：

```
application/x-www-form-urlencoded   不能使用form-data  
Copy
grant_type=authorization_code
code={上一步获取的code}
redirect_uri={与授权时一致的callbackUrl}

```
示例：

```  
Copy
POST http://localhost:8080/oauth/token
Headers: Content-Type=application/x-www-form-urlencoded
Body: grant_type=authorization_code&code=abc123&redirect_uri=http://localhost:8080/callback

```

### 2. 密码模式
Postman 配置
请求方法：POST 到 http://{auth-server}/oauth/token。

Authorization 选项卡：

Type：Basic Auth，输入 client_id 和 client_secret。

Body 选项卡：

application/x-www-form-urlencoded   不能使用form-data  

```

grant_type=password
username={user}
password={password}
scope={scopes}

```

示例：

plaintext

```

POST http://localhost:8080/oauth/token
Headers: Content-Type=application/x-www-form-urlencoded
Body: grant_type=password&username=admin&password=123456&scope=read
响应：返回 access_token 和 refresh_token710。

```

### 3. 客户端凭证模式
Postman 配置
请求方法：POST 到 http://{auth-server}/oauth/token。

Authorization 选项卡：

Type：Basic Auth，输入 client_id 和 client_secret。

Body 选项卡：
```
form-data
Copy
grant_type=client_credentials
scope={scopes}
```
示例：

plaintext
Copy
```
POST http://localhost:8080/oauth/token
Headers: Content-Type=application/x-www-form-urlencoded
Body: grant_type=client_credentials&scope=read
响应：直接返回 access_token，无需用户凭证410。
```

## 二、关键配置注意事项
授权服务器配置：

确保客户端在数据库中注册（如 oauth_client_details 表），并配置 authorized_grant_types 支持对应模式710。

密码模式需开启 AuthenticationManager 支持7。

Postman 环境变量：

建议将 access_token、client_id、client_secret 设为环境变量，简化测试流程2。

## 三、常见问题解决
错误 invalid_grant：检查 code 是否过期或重复使用。

错误 unauthorized_client：确认客户端 grant_type 权限已配置。

Postman 回调问题：使用默认回调 URL https://www.getpostman.com/oauth2/callback2。

如需完整代码示例或数据库配置，可参考 Spring Authorization Server 官方文档 或上述搜索结果中的源码链


## 


方式	                        是否推荐	        适用场景	                 安全性	                  规范支持
Query Params（URL 参数）    	❌ 不推荐	    仅用于测试或 GET 请求	      ❌ 低（泄露敏感信息）	❌ 不符合 OAuth2 标准
Body（form-data）	            ❌ 不推荐	    适用于文件上传	             ❌ 可能导致认证失败	    ❌ OAuth2 服务器通常不支持
Body（x-www-form-urlencoded）	✅ 推荐      	OAuth2 认证请求	                ✅ 高	        ✅ 符合 OAuth2 标准