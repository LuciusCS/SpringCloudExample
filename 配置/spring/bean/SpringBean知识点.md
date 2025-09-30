



## Spring Bean 的线程安全性

* 默认情况下，Spring Bean 的作用域是 singleton（单例）。
* 容器只创建一个实例，所有线程都会共享这一个对象。
* 是否线程安全要看 Bean 自身。
* Spring 不会自动帮你保证线程安全。
* 无状态的 Bean：方法内部只使用局部变量，不修改共享成员变量。 例如 @Service 里纯粹做计算、调用 DAO，这种一般是线程安全的。
* 有状态的 Bean：内部有可变的成员变量（如计数器、缓存、临时数据），在多线程场景下就 不安全。

### 解决Spring Bean 的线程安全性
#### 方法 1：让 Bean 保持无状态（推荐）
* 不在单例 Bean 中存放可变共享变量。
* 如果要保存数据，就传入参数、返回结果，而不是写到 Bean 的字段里。

```
@Service
public class UserService {
    public User process(User input) {
        // 无状态，线程安全
        return new User(input.getId(), input.getName().toUpperCase());
    }
}
```

#### 方法 2：使用线程安全的数据结构 / 并发工具

用 ConcurrentHashMap、AtomicInteger、ReentrantLock 等保证并发安全。

```
@Service
public class CounterService {
    private final AtomicInteger counter = new AtomicInteger(0);

    public int increase() {
        return counter.incrementAndGet();
    }
}

```

#### 方法 3：改变 Bean 的作用域
```
@Scope("prototype")
@Component
public class SessionData {
    private int value;
    // 每次注入都会 new 一个实例，不会被共享
}
```

#### 方法 4：使用 ThreadLocal

```
@Service
public class TenantHolder {
    private static final ThreadLocal<String> TENANT = new ThreadLocal<>();

    public void setTenant(String tenantId) { TENANT.set(tenantId); }
    public String getTenant() { return TENANT.get(); }
    public void clear() { TENANT.remove(); }
}
```

Spring Bean 默认不是线程安全的。
判断依据：单例 Bean 是否保存了共享的可变状态。
处理思路 7 种：
* 保持无状态（推荐）。
* 使用线程安全数据结构（ConcurrentHashMap、AtomicXXX）。
* 使用 ThreadLocal。
* 调整 Bean 作用域（prototype/request）。
* 加锁（synchronized、ReentrantLock）。
* 使用不可变对象（final）。
* 借助事务隔离级别解决并发冲突。



## Spring 三级缓存设计

Spring 在 DefaultSingletonBeanRegistry 中使用了 三级缓存 来解决循环依赖问题：
(1) 一级缓存：singletonObjects
存放完全初始化好的单例 Bean。
Bean 已经实例化 + 属性注入 + 初始化完成，可以直接使用。
(2) 二级缓存：earlySingletonObjects
存放“提前暴露的单例 Bean 实例”（半成品对象）。
这些 Bean 可能还没完成依赖注入，但至少已经实例化了。
用于解决 A → B → A 这种循环引用时，先把“未初始化完成的 A”放到这里，B 就能拿到 A 的引用。
(3) 三级缓存：singletonFactories
存放对象工厂（ObjectFactory），主要用来创建代理对象（AOP 场景）。
只有真正需要时，才从三级缓存里调用工厂方法生成对象，然后放到二级缓存。


## BeanNameAware 接⼝ 和 BeanClassLoaderAware 接⼝
BeanFactoryAware 接⼝  其他 *.Aware 接⼝



## Bean的生命周期