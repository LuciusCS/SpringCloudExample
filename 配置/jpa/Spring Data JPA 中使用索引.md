

```
import jakarta.persistence.*;

@Entity
@Table(
    name = "user",
    indexes = {
        @Index(name = "idx_name_age", columnList = "name, age"),
        @Index(name = "idx_email", columnList = "email", unique = true)
    }
)
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    private Integer age;

    @Column(nullable = false, unique = true)
    private String email;

    // getter & setter 省略
}

```

```
public interface UserRepository extends JpaRepository<User, Long> {

    // 方法名自动推导查询（注意字段顺序与索引一致）
    List<User> findByNameAndAge(String name, Integer age);

    // 或使用 @Query 自定义查询
    @Query("SELECT u FROM User u WHERE u.name = :name AND u.age = :age")
    List<User> queryByNameAndAge(@Param("name") String name, @Param("age") Integer age);
}

```