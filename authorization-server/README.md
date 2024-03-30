


## 用于表示认证服务器


## 授权码认证
```shell
'http://localhost:8004/oauth2/authorize?client_id=messaging-client&client_secret=secret&response_type=code&redirect_uri=www.baidu.com'
'http://localhost:8004/oauth2/authorize?client_id=messaging-client&client_secret=secret&response_type=code&redirect_uri=http://127.0.0.1:8080/authorized'
```


## 这一个 Module 如果添加了 spring-cloud-starter-gateway 

将会在认证/登陆时出现 An expected CSRF token cannot be found


## 如果不指定 server.port

将默认使用 8080端口


## 在 OAuth2TokenEndpointFilter 中配置了 OAuth2 默认请求路经

```
 private static final String DEFAULT_TOKEN_ENDPOINT_URI = "/oauth2/token";

```


## authorization-server、authorization-resource、authorization-client
三个微服务需要先启动 authorization-server 否则另外两个都启动不了