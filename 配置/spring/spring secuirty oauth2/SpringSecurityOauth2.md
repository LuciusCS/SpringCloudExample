

参考 https://github.com/ReLive27/spring-security-oauth2-sample

结合 文章 https://relive27.github.io/blog/persisrence-oauth2-client



下面的可能要删除了



参考资料

https://github.com/NotFound403/id-server


https://mainul35.medium.com/oauth2-with-spring-part-3-authorizing-oidc-client-with-via-authorization-code-grant-from-spring-67769f9dd68a  带源码


重要的微服务

https://github.com/pig-mesh/pig




https://blog.csdn.net/vaevaevae233/article/details/127082480



1.OAuth2 的常见使用场景
三方登录：当你自己开发的系统需要引入微信、qq、支付宝、钉钉等第三方登录的时候
开放平台：当你自己开发的系统需要开放一些接口，用来给第三方应用去查询使用相关的信息
sso单点登录：当你自己开发的系统是在微服务环境下使用sso相关的场景
第一种就可以把自己开发的系统理解为Gitee，用户可以使用微信、QQ等第三方登录进行登录；

第二种就可以把自己开发的系统理解为微信、QQ，用户可以在第三方应用Gitee中调用微信、QQ进行授权，然后通过gitee使用微信开放的功能；

第三种就可以把自己开发的系统理解为一个大系统（仅做举例），这个大系统提供CRM系统、ERP系统、OA协同系统，普通的使用时，用户在使用前需要在各个系统进行登录，然后才能使用；而使用sso单点登录后，用户只需要在一个系统中登录，之后使用其他系统时就可以直接访问，无需再次登录；


2.OAuth2四种授权模式的使用场景
1. 授权码模式：安全性高，使用率高，流程复杂。适用于有自己的服务器的应用（前端服务或者后端服务），它是一个一次性的临时凭证，用来换取  access_token 和  refresh_token。一旦换取成功， code 立即作废，不能再使用第二次。对安全性要求较高，web项目中一般使用授权码模式；
2. 简化模式：流程简单，简化模式适用于纯静态页面应用，该模式下， access_token 容易泄露且不可刷新，不安全；
3. 密码模式：用户向客户端提供自己的用户名和密码，客户端再向 "服务商提供商" 换取  access_token ，极度不安全，需要高度信任第三方应用；适用于其他授权模式都无法采用的情况；原生APP可以使用，web不建议使用
4. 客户端模式：授权维度为应用维度，而不是用户维度。因此有可能多个用户共用一个Token的情况。适用于应用维度的共享资源。适用于服务器之间交互，不需要用户参与。

原文链接：https://blog.csdn.net/vaevaevae233/article/details/127082480



重要的开源库

https://github.com/deepakbhalla/springboot-oauth2-jwt-and-basic-auth-security


https://github.com/m-thirumal/oauth-authorization-server/tree/main


https://github.com/Baeldung/spring-security-oauth/tree/master

https://github.com/eazybytes/springsecurity6/tree/3.3.0


https://github.com/eazybytes/spring-security?tab=readme-ov-file