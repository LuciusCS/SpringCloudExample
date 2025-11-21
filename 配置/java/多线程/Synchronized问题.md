

## 如果我用synchronized锁一个Integer对象，不能保证线程安全


重点：锁的原理：synchronized锁的是对象实例，不是变量

```java

public class IntegerLockProblem {
    private static Integer count = 0;
    
    public void increment() {
        synchronized(count) {
            count++;
        }
    }
}


// 问题出在count++这行代码，它实际上做了三件事：
// 1. 读取count的值
// 2. 把count+1
// 3. 把结果赋给count

// 但关键在于：Integer是不可变对象！
// count++相当于：count = Integer.valueOf(count.intValue() + 1);

// 每次++都会创建新的Integer对象！
// 这就导致每个线程锁的是不同的对象！

```


### 正确写法

```java
// 方案1：锁类对象（但性能差）
public void increment() {
    synchronized(IntegerLockProblem.class) {
        count++;
    }
}

// 方案2：使用专门的锁对象
private final Object lock = new Object();
public void increment() {
    synchronized(lock) {
        count++;
    }
}

// 方案3：使用AtomicInteger（推荐）
private AtomicInteger count = new AtomicInteger(0);
public void increment() {
    count.incrementAndGet();
}

```

### 可以使用 AtomicInteger