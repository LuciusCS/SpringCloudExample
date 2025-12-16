package com.example.demo.status;

/**
 * 0  UNPAID
 * 1  PAYING
 * 2  PAID
 * 3  PAY_FAILED
 * 4  REFUND_SUCCESS
 */
public enum PayStatus {
    UNPAID(0),
    PAYING(1),
    PAID(2),
    PAY_FAILED(3),
    REFUND_SUCCESS(4);
}
