


在使用RedisConfig将存储到redis中的数据序列化事，会造成网络请求的json数据序列化报错，因此需要WebConfig

报错内容为

```
2025-03-19 14:58:35 - DEBUG [http-nio-8004-exec-2] o.s.w.m.HandlerMethod - getMethodArgumentValues - 235 - Could not resolve parameter [0] in public boolean com.example.auth.controller.OidcUserInfoController.addUser(com.example.auth.bean.User): JSON parse error: Could not resolve subtype of [simple type, class com.example.auth.bean.User]: missing type id property '@class'
2025-03-19 14:58:35 - WARN [http-nio-8004-exec-2] o.s.w.s.m.s.DefaultHandlerExceptionResolver - logException - 247 - Resolved [org.springframework.http.converter.HttpMessageNotReadableException: JSON parse error: Could not resolve subtype of [simple type, class com.example.auth.bean.User]: missing type id property '@class']
2025-03-19 14:58:35 - DEBUG [http-nio-8004-exec-2] o.s.w.s.DispatcherServlet - logResult - 1138 - Completed 400 BAD_REQUEST
2025-03-19 14:58:35 - DEBUG [http-nio-8004-exec-2] o.s.s.w.FilterChainProxy - doFilterInternal - 223 - Securing POST /error
```