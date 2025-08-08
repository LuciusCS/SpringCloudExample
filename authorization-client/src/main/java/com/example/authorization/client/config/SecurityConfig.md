


| 问题               | 回答                                                    |
| ---------------- | ----------------------------------------------------- |
| 使用你这段配置能自动续期吗？   | ✅ 能，已配置好了 `client_credentials` 模式的自动续期                |
| 续期是怎么触发的？        | WebClient 请求时，如果 token 过期则自动续期                        |
| token 储存在哪里？     | 默认是本地内存，`InMemoryOAuth2AuthorizedClientService`       |
| 多实例时能共享 token 吗？ | ❌ 默认不能，需要用 Redis 等自定义 `OAuth2AuthorizedClientService` |
| 是否需要手动刷新？        | ❌ 不需要，Spring 会自动处理                                    |


暂时不需要 TokenRefreshService.java 主动刷新Token  待验证

可以通过 TokenPrintingFilter 拦截Token查看自动续期是否生效
