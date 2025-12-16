package com.example.demo.status;

/**
 * 0  NEW         待支付
 * 1  PAID        已支付（钱已到账）
 * 2  FINISHED    已完成（履约完成）
 * 3  CANCELED    已取消（未支付）
 * 4  REFUNDING   退款中
 * 5  REFUNDED    已退款
 */
public enum OrderStatus{
    NEW(0),
    PAID(1),
    FINISHED(2),
    CANCELED(3),
    REFUNDING(4),
    REFUNDED(5);
}
