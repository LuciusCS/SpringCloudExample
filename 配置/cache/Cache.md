

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

## `jakarta.persistence.Cacheable`用来标识实体类是否启用 二级缓存（Second Level Cache）

绝大多数情况下，不建议使用 Hibernate 二级缓存。
如果你已经有 Redis / Caffeine，推荐使用 Spring Cache / 自定义缓存层来管理缓存，更灵活、更可控、可扩展性更强。


| 特性     | Hibernate 二级缓存        | Spring Cache（基于 Redis/Caffeine） |
| ------ | --------------------- | ------------------------------- |
| 缓存作用域  | **JPA 实体对象（PO）**      | **DTO / 自定义对象 / 任意数据**          |
| 缓存触发方式 | JPA 查询（如 `.findById`） | 任意方法（如 Service 层 `@Cacheable`）  |
| 缓存粒度   | Entity 级别             | 方法级别或业务级别                       |
| 缓存控制   | 框架自动管理（难控制）           | 可手动管理、失效、更新                     |
| 可维护性   | 差（黑盒，调试难）             | 好（透明、清晰、易维护）                    |
| 适合对象   | 静态数据（字典、地区）           | 任何查询数据（支持条件、分页）                 |
| 分布式支持  | 差（配置复杂、不推荐）           | 好（Redis 原生支持）                   |


### 不推荐 Hibernate 二级缓存的原因
#### 失效机制复杂：
修改实体数据后，缓存是否自动失效依赖具体配置，出错率高。
#### 调试困难：
很难追踪缓存命中/失效日志，开发定位问题不方便。
#### 不支持复杂查询（如分页/条件）：
Hibernate 二级缓存只缓存按 ID 查询的数据，不能缓存复杂查询结果。
#### 分布式环境不友好：
不适合在多节点部署场景使用，容易缓存不一致。
#### 已经有更好的替代品（Spring Cache）：
@Cacheable + Redis/Caffeine，支持 TTL、条件缓存、主动失效、统计等等。
