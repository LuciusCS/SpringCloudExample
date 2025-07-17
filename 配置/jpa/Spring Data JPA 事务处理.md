
## JPA 事务处理

`@Transactional` 可以添加到类上，也可以添加到方法上，只是事务管理的粒度不同

### 方法一：
```java
@Service
public class UserService {

    @PersistenceContext  // 使用 @PersistenceContext 确保 EntityManager 与事务绑定
    private EntityManager entityManager;

    @Transactional  // 确保方法的操作是在事务中执行
    public void updateUser(Long userId) {
        // 获取持久化实体
        User user = entityManager.find(User.class, userId);
        user.setName("Updated Name");  // 修改实体

        // 由于是在事务中，所有的变化会在事务提交时同步到数据库
    }
}

```

### 方法二：

不推荐

```java

@Service
public class UserService {
 ///不推荐
  @Resource
    private EntityManager entityManager;

    @Transactional  // 确保方法的操作是在事务中执行
    public void updateUser(Long userId) {
        // 获取持久化实体
        User user = entityManager.find(User.class, userId);
        user.setName("Updated Name");  // 修改实体

        // 由于是在事务中，所有的变化会在事务提交时同步到数据库
    }
}


```

### 方法三：

```java

@Service
public class UserService {

    @Resource
    private EntityManager entityManager;

    public void updateUser(Long userId) {
        // 手动管理事务
        EntityTransaction transaction = entityManager.getTransaction();
        try {
            transaction.begin();  // 手动开启事务

            // 获取并修改实体
            User user = entityManager.find(User.class, userId);
            user.setName("Updated Name");

            transaction.commit();  // 手动提交事务
        } catch (RuntimeException e) {
            transaction.rollback();  // 事务回滚
            throw e;  // 抛出异常
        }
    }
}
```



使用 @Resource 注入 EntityManager 是可以的，但不推荐，因为它不会自动绑定事务上下文。如果你需要更细粒度的控制，可以手动管理事务，但这通常不推荐。
使用 @PersistenceContext 注入 EntityManager 是更好的做法，它会自动与当前事务绑定，并确保事务的管理更加简洁和自动化。



127.0.0.1:8004/druid/login.html 