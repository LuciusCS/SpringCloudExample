

## Logger 的使用

```
    private static final Logger logger = LoggerFactory.getLogger(OrderCleanupJob.class);
         logger.debug("开发环境清理过期订单"); // 开发环境可见
        logger.info("生产环境清理过期订单"); // 生产环境可见
        logger.error("生产环境清理过期订单异常"); // 错误日志

```