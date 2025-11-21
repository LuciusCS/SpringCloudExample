

## 两级缓存

参考代码 
 order-server 微服务中
` com.example.order.config.cache.CacheConfig`


### cacheManager 的调用位置在哪？
   配置了一个 CompositeCacheManager 作为 Spring 应用的 默认缓存管理器，并且它被 @Primary 标记。
   Spring Cache 注解（如 @Cacheable, @CachePut, @CacheEvict）在运行时会默认使用 容器中唯一或者被标记为 @Primary 的 CacheManager。
   也就是说，cacheManager 并不需要你在业务代码里显式调用，Spring 框架会自动根据配置选择它来处理缓存的存取。 
### @Cacheable 和 @CachePut 是如何处理一级和二级缓存的？
   CompositeCacheManager 的核心就是将多个 CacheManager 组合起来。比如你组合了：
   CaffeineCacheManager（一级缓存）
   RedisCacheManager（二级缓存）
   Spring Cache 调用缓存操作时，会依次调用 CompositeCacheManager 维护的各个子 CacheManager：
   读缓存时，按顺序查询一级缓存，如果未命中，继续查二级缓存，依次类推。
   写缓存时，会同时写入所有子 CacheManager 对应的缓存。
   清理缓存时，同理，会同时清理所有子 CacheManager 的缓存。
### 为什么看起来一级缓存和二级缓存都是同时有数据，且同时删除？
   因为 CompositeCacheManager 在调用写缓存或清理缓存操作时，是对它维护的所有 CacheManager 统一操作的：
   写缓存时，一级和二级缓存都会被写入。
   删除缓存时，一级和二级缓存都会被删除。


## 通过日志调试看缓存调用

```
logging.level.org.springframework.cache=DEBUG
logging.level.com.github.benmanes.caffeine=DEBUG
logging.level.org.springframework.data.redis.cache=DEBUG
```


