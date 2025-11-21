


在现代系统中，使用 Redis 缓存结果后，MySQL 查询缓存通常没有必要启用。Redis 能够提供更高的性能、更灵活的管理和更好的扩展性。
而 MySQL 查询缓存已经逐渐被淘汰，在复杂场景下效果不如 Redis 明显。

## JPA中使用索引

在 JPA 实体类中使用 @Table(indexes = …)（用于建表时）

优点：适合项目初期让 Hibernate 自动建表。
注意事项：
只有在 spring.jpa.hibernate.ddl-auto=create 或 update 时才会起作用；
不会自动同步已有数据库；
如果数据库已经有表了，加了这个注解也不会自动建索引。

## 直接通过 MySql 建立索引

## JPA通过索引进行查询


```java


//只要查询中包含联合索引字段，并符合最左前缀原则，索引就会生效。

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    // 会使用联合索引（username, email）
    Optional<User> findByUsernameAndEmail(String username, String email);

    // 也可能用到索引（只查询 username，仍符合最左前缀）
    List<User> findByUsername(String username);

    // ❌ 只查 email 通常不会用上这个联合索引（不符合最左前缀）
    List<User> findByEmail(String email);
}

```