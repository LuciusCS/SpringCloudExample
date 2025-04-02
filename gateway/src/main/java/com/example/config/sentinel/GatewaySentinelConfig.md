



在 Spring Cloud Gateway 中集成 Sentinel 实现限流、熔断、降级和动态规则变更

可以通过手动配置路由的方式，也可以通过自动配置路由的方式


默认情况下 Sentinel 只能接收到 Nacos 推送的消息，但不能将自己控制台修改的信息同步给 Nacos，如下图所示：

在 Sentinel 1.8.0+ 版本中，官方提供了一个 sentinel.datasource 相关配置，可以将控制台的规则同步到 Nacos 等外部配置中心。配置方法如下：