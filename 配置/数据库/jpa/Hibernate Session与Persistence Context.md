


## Persistence Context（持久化上下文）就是 Hibernate Session 实现的 JPA 标准接口。


### 🔸1. 什么是 Persistence Context？
Persistence Context 是 JPA 规范中定义的概念，它代表一个用于管理实体对象及其生命周期的上下文环境。
简单理解：

它是一个临时的“实体对象缓存”，用来跟踪哪些对象是“持久态”的。
它保证在一个上下文中，同一个主键只会有一个实体实例（对象唯一性）。

### 🔸2. 什么是 Hibernate Session？
Hibernate Session 是 Hibernate 自己的实现类，具体实现了 JPA 的 Persistence Context 接口。

| 概念                   | 来自           | 本质                           |
| -------------------- | ------------ | ---------------------------- |
| `PersistenceContext` | JPA 标准       | 接口/概念                        |
| `Session`            | Hibernate 实现 | 实现类（`org.hibernate.Session`） |

关联关系

```java

// JPA 的 EntityManager 接口
EntityManager em = ...

// EntityManager 底层其实封装了 Hibernate 的 Session
Session session = em.unwrap(Session.class);

```

### 3. 什么是 EntityManager

EntityManager 是管理 Persistence Context 的 API，它通过 Persistence Context 来跟踪和管理实体对象的生命周期（如新增、修改、删除、查询等）。

EntityManager 是 JPA 规范的统一入口。
EntityManager 内部就是使用 Hibernate 的 Session 来实现实体的管理与缓存。
所以Persistence Context 和  Hibernate Session 两个是接口与实现的关系。


| 项目      | Persistence Context  | Hibernate Session                            |
| ------- | -------------------- | -------------------------------------------- |
| 来源      | JPA 规范               | Hibernate 实现                                 |
| 类型      | 概念（接口）               | 实际类                                          |
| 管理方式    | 由 `EntityManager` 管理 | Hibernate 的 `Session` 实现                     |
| 是否可直接使用 | 不能直接实例化              | 可通过 `EntityManager.unwrap(Session.class)` 使用 |
| 是否可配置   | JPA 管理事务/作用域         | Hibernate 支持更丰富配置（如 flushMode、缓存策略等）         |


| 概念                  | 一句话记忆                   |
| ------------------- | ----------------------- |
| Persistence Context | JPA 规定的“缓存+实体管理”容器，接口层  |
| Hibernate Session   | Hibernate 提供的实现，真正做事的角色 |


### 一般情况下你使用的都是 EntityManager
这些操作背后其实调用的就是 Hibernate 的 Session，你通常不需要关心。

```
// 常规的保存
entityManager.persist(entity);
// 查询
entityManager.find(Station.class, id);

```

### 以下场景下，你可能需要直接使用 Hibernate 的 Session

#### 1. 使用 Hibernate 特有 API（JPA 没有）

StatelessSession（无状态会话）,适合大批量插入/更新，不走一级缓存。

```java

StatelessSession session = sessionFactory.openStatelessSession();
```
####  2. 批量插入/更新优化
相比 entityManager.persist()，这样可以避免内存溢出和性能瓶颈。

```
Session session = entityManager.unwrap(Session.class);

for (int i = 0; i < list.size(); i++) {
    session.save(list.get(i));
    if (i % 50 == 0) {
        session.flush();  // 强制提交到数据库
        session.clear();  // 清空一级缓存，释放内存
    }
}
```

####  3. 手动控制 FlushMode
FlushMode 是 Hibernate 提供的特性，可以控制什么时候把一级缓存同步到数据库。
```
Session session = entityManager.unwrap(Session.class);
session.setFlushMode(FlushMode.MANUAL);  
```

#### 4. 访问 Hibernate 的二级缓存 API
JPA 本身没有对二级缓存的操作接口，而 Hibernate 的 Session 有：
```
Session session = entityManager.unwrap(Session.class);
SessionFactory sf = session.getSessionFactory();
sf.getCache().evict(Station.class);  // 清除二级缓存中的 Station 实体
```
#### 5. 使用 Criteria API（Hibernate 原生）

虽然 JPA 也有自己的 Criteria API，但 Hibernate 有扩展版，例如：
（注意：Hibernate Criteria API 在新版本中逐步废弃，推荐使用 JPA 或 QueryDSL）
```
Session session = entityManager.unwrap(Session.class);
Criteria criteria = session.createCriteria(Station.class);
```

#### 6. 强制获取实体对象对应的代理（Proxy）
相比之下，entityManager.find() 是立即加载。
```
Session session = entityManager.unwrap(Session.class);
Station proxy = session.load(Station.class, id);  // 返回一个懒加载代理对象
```

### 总结：什么时候用 Hibernate Session
“普通操作用 JPA，性能优化靠 Hibernate，操作缓存找 Session，底层扩展别忘了 unwrap。”

| 场景                   | 是否推荐用 `Session`                           |
| -------------------- | ----------------------------------------- |
| 普通增删改查               | ❌ 不需要，`EntityManager` 足够                  |
| 批量操作（数千条记录）          | ✅ 推荐使用 Hibernate `Session` 优化             |
| 需要手动控制 flush         | ✅ 使用 Hibernate API 更灵活                    |
| 操作二级缓存               | ✅ 只有 Hibernate 有公开 API                    |
| 使用 Hibernate 独有的扩展功能 | ✅ 例如 StatelessSession、interceptor、自定义 SQL |

### 如果事务未提交或未 flush，查询的数据是旧的还是新的

#### 情况一：在事务内部查询，且是同一个 Persistence Context（同一个事务）
✅ 结果：输出是 "New Name" —— 因为：
Hibernate 有一级缓存（Persistence Context），同一个事务内查询同一个对象，会命中缓存，不访问数据库。
即使没有 flush 到数据库，也会反映你在内存中对对象的修改。
```
@Transactional
public void updateAndQuery(Long id) {
    StationPO station = stationRepository.findById(id).orElseThrow();
    station.setName("New Name");

    // 再次查询同一个 id
    StationPO again = stationRepository.findById(id).orElseThrow();
    System.out.println(again.getName()); // 输出 "New Name"
}
```

#### 情况二：在事务外部，或另一个事务中查询
🚫 结果：查到的是旧数据 —— 因为：
Hibernate 的变更还没 flush 到数据库（或事务未提交），对数据库是不可见的。
数据库层面没有变更，其他事务/请求当然查不到。
```
stationService.updateStation(id); // 事务未提交
// 马上发起 HTTP 请求或另一个线程读取同一个 id
```


### Hibernate 什么时候会自动 flush

| 场景                                          | 是否触发 flush        |
| ------------------------------------------- | ----------------- |
| ✅ **事务提交前**                                 | 一定 flush          |
| ✅ **执行 JPQL / HQL / 原生 SQL（查询）**，并且涉及脏数据相关表 | 会先 flush，再查询      |
| ✅ 调用 `flush()`                              | 手动触发              |
| ❌ 查询与修改无关的表                                 | 不会触发 flush        |
| ❌ 事务中未执行查询，只操作实体对象                          | 不会自动 flush，直到事务提交 |
