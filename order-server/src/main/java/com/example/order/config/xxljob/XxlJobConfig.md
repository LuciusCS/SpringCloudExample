

## 配置问题

xxl-job 的配置信息，仅仅在yml中进行配置，是不会生效的，


❗为什么 application.yml 中的配置无效？
Spring Boot 的配置绑定不会自动作用于 XxlJobSpringExecutor，因为：
XxlJobSpringExecutor 本身不是一个带有 @Component 或 @ConfigurationProperties 注解的类。
你在手动构造这个 bean，而不是让 Spring Boot 自动注入它的属性