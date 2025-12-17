package com.example.demo.service;

import com.example.demo.bean.po.OrderPO;
import com.example.demo.bean.po.PaymentRecordPO;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRecordRepository;
import com.example.demo.status.OrderStatus;
import com.example.demo.status.PayStatus;
import com.example.demo.util.WechatNotify;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class PayCallbackService {

    private final OrderRepository orderRepo;
    private final PaymentRecordRepository payRepo;

    @Transactional
    public void handleWechatPaySuccess(
            WechatNotify notify,
            String rawNotify) {

        String orderNo = notify.getOutTradeNo();

        // 1️⃣ 查询订单（加锁，防并发）
        OrderPO order = orderRepo.findByOrderNoForUpdate(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        // 2️⃣ 幂等判断（最关键）
        if (order.getPayStatus() == PayStatus.PAID.getCode()) {
            return; // 已处理过，直接返回
        }

        // 3️⃣ 金额校验（绝不能省）
        if (order.getPayAmount()
                .compareTo(notify.getTotalAmount()) != 0) {
            throw new RuntimeException("支付金额不一致");
        }

        // 4️⃣ 保存支付流水
        PaymentRecordPO record = new PaymentRecordPO();
        record.setOrderNo(orderNo);
        record.setOutTradeNo(notify.getOutTradeNo());
        record.setTransactionId(notify.getTransactionId());
        record.setPayType("WECHAT");
        record.setPayAmount(notify.getTotalAmount());
        record.setPayStatus(PayStatus.PAID.getCode());
        record.setRawNotify(rawNotify);
        record.setPayTime(LocalDateTime.now());

        payRepo.save(record);

        // 5️⃣ 更新订单状态（状态机）
        order.setPayStatus(PayStatus.PAID.getCode());
        order.setOrderStatus(OrderStatus.PAID.getCode());
        order.setPayTime(LocalDateTime.now());

        orderRepo.save(order);
    }
}
