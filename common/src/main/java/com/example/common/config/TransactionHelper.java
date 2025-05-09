package com.example.common.config;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.function.Function;

/**
 * 解决问题1:@Transactional的局限性：当前对象调用对象自己的方法不起作用的场景
 *      @Transactional 通过动态代理来实现事务管理，而 Spring 默认使用的是 JDK 动态代理或者 CGLIB 代理。
 *     如果一个方法内部调用了同一类中的其他方法（即方法调用是直接的，不会经过代理），那么事务就不会生效。
 *     因为事务操作会通过代理方式处理，而不是直接调用方法。
 *
 * 解决问题2:@Transactional的局限性：默认只对uncheckException进行会滚，其他不进行会滚
 */

@Component
class TransactionHelper {

    /**
     * 默认事务传播行为：REQUIRED（如果事务存在，则加入当前事务；如果不存在，则创建新事务）
     */
    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.REQUIRED)
    public <T, R> R transactional(Function<T, R> function, T t) {
        return function.apply(t);
    }

    /**
     * 使用自定义事务传播行为
     */
    @Transactional(rollbackOn = Exception.class)
    public <T, R> R transactionalWithPropagation(Transactional.TxType propagation, Function<T, R> function, T t) {
        return function.apply(t);
    }

    /**
     * 只读事务，用于查询操作，优化性能
     */
    @Transactional(rollbackOn = Exception.class, value = Transactional.TxType.SUPPORTS) // 假设查询操作可以支持当前事务
    public <T, R> R readOnlyTransactional(Function<T, R> function, T t) {
        return function.apply(t);
    }
}

/***
 * 在 Service 中进行调用

 @Autowired
 private TransactionHelper transactionHelper;

public UserInfo save(Long userId) {
    return transactionHelper.transactional((uid) -> this.calculate(uid), userId);
}


 */
