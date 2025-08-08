


## 在 Spring 开发中较少直接面对并发和线程安全问题，主要归功于 Spring 框架的精心设计和现代 Java 开发范式的转变

### Spring 的架构设计消除了常见并发隐患

#### 默认的单例 Bean 作用域

Spring 管理的 Bean 默认为单例（Singleton）

但通过依赖注入 + 无状态设计保证线程安全：

```
@Service
public class OrderService {
    // 线程安全：无成员变量
    public Order createOrder(OrderRequest request) {
        // 仅使用局部变量和方法参数
    }
}
```

#### 请求隔离机制

每个 HTTP 请求分配独立线程（Tomcat 线程池）
请求间状态通过 ThreadLocal 隔离：

```
// Spring Security 的当前用户信息
SecurityContextHolder.getContext(); // ThreadLocal 实现
```

#### 事务管理的线程绑定

@Transactional 通过 ConnectionHolder 绑定到当前线程
数据库连接不会跨线程共享