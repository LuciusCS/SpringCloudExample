

## `jakarta.persistence.Cacheable` 和 `org.springframework.cache.annotation.Cacheable`



1.  `jakarta.persistence.Cacheable`
   全路径：jakarta.persistence.Cacheable（以前是 javax.persistence.Cacheable）
   作用：这是 JPA 里的注解，用来标识实体类是否启用 二级缓存（Second Level Cache）
   用法：标注在实体类上，告诉 JPA 实现（如 Hibernate）是否缓存实体对象，缓存范围是 ORM 框架一级缓存（Session/EntityManager缓存）之外的第二级缓存，缓存数据库查询结果，减少数据库访问。
   ```
   @Entity
   @Cacheable(true)
   public class Station {
   // ...
   }
   ```
   适用场景：
   你使用 JPA 或 Hibernate，需要开启实体对象的二级缓存。
   主要目的是提高数据库访问性能，避免频繁查询数据库。
2. `org.springframework.cache.annotation.Cacheable`
   全路径：org.springframework.cache.annotation.Cacheable
   作用：Spring Framework 提供的通用缓存注解，用于方法级别缓存，缓存方法的返回结果，支持各种缓存实现（Redis、Ehcache、Guava等）
   用法：标注在 Service 或其他 Bean 的方法上，缓存该方法调用结果，避免重复执行，提高应用层性能。
  
   ```
   @Service
   public class StationService {

   @Cacheable(value = "stationCache", key = "#id")
   public StationDTO getStationById(Long id) {
   // 调用数据库或远程接口
   }
   }
   ```
   适用场景：
   你想缓存方法调用结果，减少数据库、API等重复请求。
   缓存数据粒度更灵活，支持过期、失效策略。
   支持多种缓存存储，不依赖 ORM。