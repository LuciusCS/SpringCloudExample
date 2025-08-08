

| 缓存级别                                | 应用场景           | 是否推荐          |
| ----------------------------------- | -------------- | ------------- |
| 一级缓存                                | 所有事务内数据库操作     | ✅ 强烈推荐（默认开启）  |
| 二级缓存（Hibernate）                     | 实体类非常稳定，且命中率高  | ⚠️ 可选（适合静态数据） |
| 手动缓存（Spring Cache + Redis/Caffeine） | DTO、组合数据、多条件缓存 | ✅ 推荐          |


## Hibernate 缓存处理

在实际开发的过程中，会使用 Hibernate 的一级缓存，而不推荐使用Hibernate的二级缓存


### 经常用到一级缓存的例子

```java

@Transactional
public void updateStation(Long id) {
    StationPO station = stationRepository.findById(id).orElseThrow(); // 查询数据库
    station.setName("New Name"); // 修改字段

    // 注意：这里没有调用 save！
    // 但是事务提交时，Hibernate 会自动检测修改并执行 UPDATE
}
```

#### 没有 save 也能更新
因为：
第一次查询时，Hibernate 把实体放进了一级缓存（Persistence Context）
你修改它，相当于修改了缓存里的对象
在事务提交时，Hibernate 自动执行“脏检查”，发现有改动，生成 SQL 执行更新


