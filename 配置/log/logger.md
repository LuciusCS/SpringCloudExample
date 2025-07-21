

## Logger 的使用

```
    private static final Logger logger = LoggerFactory.getLogger(OrderCleanupJob.class);
         logger.debug("开发环境清理过期订单"); // 开发环境可见
        logger.info("生产环境清理过期订单"); // 生产环境可见
        logger.error("生产环境清理过期订单异常"); // 错误日志

```


## 方便方法, 使用@Slf4j 注解

```java

@Slf4j
public class TokenRefreshService {
    private void logTokenDetails(String message, OAuth2AccessToken token) {
        if (token == null) return;

        long minutesToExpire = Duration.between(
                Instant.now(), token.getExpiresAt()).toMinutes();

        log.info(message);
        log.info("  Token value: " + token.getTokenValue());
        log.info("  maskToken value: " + maskToken(token.getTokenValue()));
        log.info("  Expires at: " + token.getExpiresAt());
        log.info("  Minutes until expiration: " + minutesToExpire);
        log.info("-----------------------------------------");
    }
}

```