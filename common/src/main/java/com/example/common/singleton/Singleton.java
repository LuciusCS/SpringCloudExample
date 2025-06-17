package com.example.common.singleton;

public class Singleton {

    // 静态内部类，只有在调用时才会加载
    private static class SingletonHolder {
        // 创建唯一的单例对象
        private static final Singleton INSTANCE = new Singleton();
    }

    // 私有构造函数
    private Singleton() {}

    public static Singleton getInstance() {
        return SingletonHolder.INSTANCE;
    }
}

/**
 *
 * 单例模式进行序列化，采用注册式单例模式：枚举式单例模式、容器式单例模式
 *
 * 枚举式单例模式是 Effective Java 书中推荐的， 容器式单例模式是非线程安全的
 *
 */

/**

 特点：

 延迟加载：静态内部类 SingletonHolder 只有在调用 getInstance() 方法时才会被加载，从而避免了饿汉式的内存浪费。
 线程安全：静态内部类是由 JVM 保证线程安全的，因为它是类加载机制的一部分，JVM 会确保静态内部类在多线程环境下只会被加载一次。
 没有内存浪费：只有在第一次调用 getInstance() 时，静态内部类才会被加载并初始化实例，因此如果 getInstance() 从未被调用，单例对象就不会被创建，从而避免了不必要的内存浪费。
 适用场景：适用于我们不确定是否需要该单例，或者需要延迟加载并且保证线程安全的场景。


 为什么静态内部类单例不会造成内存浪费？

 静态内部类的加载机制：
 静态内部类 SingletonHolder 在类 Singleton 被加载时，并不会立即加载 SingletonHolder 类。
 SingletonHolder 只有在调用 getInstance() 方法时才会被加载。类加载是惰性地进行的，只有在第一次需要访问时才会进行初始化。
 JVM的类加载机制保证线程安全：
 JVM 会在加载类时保证该类的加载过程是线程安全的。当 SingletonHolder 类被加载时，INSTANCE 会被初始化，而 JVM 会确保 INSTANCE 只会被初始化一次，因此线程安全得以保证。
 内存效率：
 由于静态内部类只有在第一次访问时才会被加载，在没有调用 getInstance() 的情况下，SingletonHolder 类并不会被加载，也不会创建 INSTANCE 实例。
 相比之下，饿汉式单例在类加载时就会实例化对象，因此即使从未使用该对象，也会占用内存。



 特性	        饿汉式单例                     	懒汉式单例	               双重锁检测单例	                           静态内部类单例
 创建时机	类加载时立即创建实例	             第一次调用时创建实例	         第一次调用时创建实例	                         第一次调用时创建实例
 线程安全	    线程安全    	             线程安全（使用 synchronized）  	线程安全（使用 volatile 和 synchronized）	        线程安全（JVM 保证）
 内存浪费	可能存在内存浪费	                    避免了内存浪费	            避免了内存浪费	                             避免了内存浪费
 性能	    启动时创建实例，性能开销小       	每次调用时加锁，性能较差	    性能较好，只有第一次创建时加锁	            性能最优，静态内部类加载时不会加锁
 实现复杂度	简单	相对复杂，需要 synchronized	较复杂，需要双重锁和 volatile	        稍复杂，需要静态内部类
 适用场景	确保程序启动时一定需要该单例	        不确定是否需要该单例，适用于懒加载	高并发场景，保证性能和线程安全	高并发场景，性能优，延迟加载



 */