


### 用于表示资源服务

资源服务器启动时，需要authorization-server先启动，否则会发生报错

```angular2html
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'securityFilterChain' defined in class path resource [com/example/authorization/resource/config/SecurityConfig.class]: Failed to instantiate [org.springframework.security.web.SecurityFilterChain]: Factory method 'securityFilterChain' threw exception with message: Unable to resolve the Configuration with the provided Issuer of "http://localhost:8004"
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:651)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiateUsingFactoryMethod(ConstructorResolver.java:639)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.instantiateUsingFactoryMethod(AbstractAutowireCapableBeanFactory.java:1335)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBeanInstance(AbstractAutowireCapableBeanFactory.java:1165)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.doCreateBean(AbstractAutowireCapableBeanFactory.java:562)
	at org.springframework.beans.factory.support.AbstractAutowireCapableBeanFactory.createBean(AbstractAutowireCapableBeanFactory.java:522)
	at org.springframework.beans.factory.support.AbstractBeanFactory.lambda$doGetBean$0(AbstractBeanFactory.java:325)
	at org.springframework.beans.factory.support.DefaultSingletonBeanRegistry.getSingleton(DefaultSingletonBeanRegistry.java:234)
	at org.springframework.beans.factory.support.AbstractBeanFactory.doGetBean(AbstractBeanFactory.java:323)
	at org.springframework.beans.factory.support.AbstractBeanFactory.getBean(AbstractBeanFactory.java:199)
	at org.springframework.beans.factory.support.DefaultListableBeanFactory.preInstantiateSingletons(DefaultListableBeanFactory.java:975)
	at org.springframework.context.support.AbstractApplicationContext.finishBeanFactoryInitialization(AbstractApplicationContext.java:959)
	at org.springframework.context.support.AbstractApplicationContext.refresh(AbstractApplicationContext.java:624)
	at org.springframework.boot.web.servlet.context.ServletWebServerApplicationContext.refresh(ServletWebServerApplicationContext.java:146)
	at org.springframework.boot.SpringApplication.refresh(SpringApplication.java:754)
	at org.springframework.boot.SpringApplication.refreshContext(SpringApplication.java:456)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:334)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1354)
	at org.springframework.boot.SpringApplication.run(SpringApplication.java:1343)
	at com.example.authorization.resource.AuthorizationResourceApplication.main(AuthorizationResourceApplication.java:19)
Caused by: org.springframework.beans.BeanInstantiationException: Failed to instantiate [org.springframework.security.web.SecurityFilterChain]: Factory method 'securityFilterChain' threw exception with message: Unable to resolve the Configuration with the provided Issuer of "http://localhost:8004"
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:177)
	at org.springframework.beans.factory.support.ConstructorResolver.instantiate(ConstructorResolver.java:647)
	... 19 more
Caused by: java.lang.IllegalArgumentException: Unable to resolve the Configuration with the provided Issuer of "http://localhost:8004"
	at org.springframework.security.oauth2.jwt.JwtDecoderProviderConfigurationUtils.getConfiguration(JwtDecoderProviderConfigurationUtils.java:168)
	at org.springframework.security.oauth2.jwt.JwtDecoderProviderConfigurationUtils.getConfigurationForIssuerLocation(JwtDecoderProviderConfigurationUtils.java:80)
	at org.springframework.security.oauth2.jwt.NimbusJwtDecoder.lambda$withIssuerLocation$2(NimbusJwtDecoder.java:226)
	at org.springframework.security.oauth2.jwt.NimbusJwtDecoder$JwkSetUriJwtDecoderBuilder.processor(NimbusJwtDecoder.java:389)
	at org.springframework.security.oauth2.jwt.NimbusJwtDecoder$JwkSetUriJwtDecoderBuilder.build(NimbusJwtDecoder.java:405)
	at org.springframework.security.oauth2.jwt.JwtDecoders.fromIssuerLocation(JwtDecoders.java:92)
	at com.example.authorization.resource.config.SecurityConfig.lambda$securityFilterChain$1(SecurityConfig.java:22)
	at org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer.jwt(OAuth2ResourceServerConfigurer.java:225)
	at com.example.authorization.resource.config.SecurityConfig.lambda$securityFilterChain$2(SecurityConfig.java:22)
	at org.springframework.security.config.annotation.web.builders.HttpSecurity.oauth2ResourceServer(HttpSecurity.java:2977)
	at com.example.authorization.resource.config.SecurityConfig.securityFilterChain(SecurityConfig.java:22)
	at com.example.authorization.resource.config.SecurityConfig$$SpringCGLIB$$0.CGLIB$securityFilterChain$0(<generated>)
	at com.example.authorization.resource.config.SecurityConfig$$SpringCGLIB$$FastClass$$1.invoke(<generated>)
	at org.springframework.cglib.proxy.MethodProxy.invokeSuper(MethodProxy.java:258)
	at org.springframework.context.annotation.ConfigurationClassEnhancer$BeanMethodInterceptor.intercept(ConfigurationClassEnhancer.java:331)
	at com.example.authorization.resource.config.SecurityConfig$$SpringCGLIB$$0.securityFilterChain(<generated>)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
	at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
	at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
	at java.base/java.lang.reflect.Method.invoke(Method.java:568)
	at org.springframework.beans.factory.support.SimpleInstantiationStrategy.instantiate(SimpleInstantiationStrategy.java:140)
	... 20 more
Caused by: org.springframework.web.client.ResourceAccessException: I/O error on GET request for "http://localhost:8004/.well-known/openid-configuration": Connection refused
	at org.springframework.web.client.RestTemplate.createResourceAccessException(RestTemplate.java:915)
	at org.springframework.web.client.RestTemplate.doExecute(RestTemplate.java:895)
	at org.springframework.web.client.RestTemplate.exchange(RestTemplate.java:740)
```