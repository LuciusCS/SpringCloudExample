

## 在Mysql层面设置

### 查看当前隔离级别

```

SELECT @@transaction_isolation;
-- 或
SELECT @@tx_isolation; -- MySQL 5.x 老版本

```

### 修改隔离级别

- 全局修改（影响所有连接）：
```
SET GLOBAL TRANSACTION ISOLATION LEVEL REPEATABLE READ;
```

- 全局修改（影响所有连接）：
```
SET SESSION TRANSACTION ISOLATION LEVEL READ COMMITTED;

```

## 在Spring Cloud 项目中使用

```
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderService {

    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void createOrder() {
        // 业务逻辑
    }
}
```

## 配置层面（比如 application.yml）

```
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/demo?useSSL=false&serverTimezone=UTC
    username: root
    password: root
  jpa:
    properties:
      hibernate.connection.isolation: 2  # 2=READ_COMMITTED, 4=REPEATABLE_READ, 8=SERIALIZABLE
```


数据库层面：MySQL 默认是 REPEATABLE READ，可通过 SET SESSION/ GLOBAL 修改。
Spring 层面：用 @Transactional(isolation=...) 精确控制方法级事务隔离级别。
微服务场景：多数情况下保持默认 REPEATABLE READ，跨服务事务用 Seata/TCC 框架控制一致性。



