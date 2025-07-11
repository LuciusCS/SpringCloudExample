


## 用于表示认证服务器


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

功能实现参考代码：  youlai-mall
源码地址：  https://github.com/youlaitech/youlai-mall
https://gitee.com/youlaiorg/youlai-mall
