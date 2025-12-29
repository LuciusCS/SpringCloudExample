package com.example.demo.service;

import com.example.demo.bean.po.OrderPO;
import com.example.demo.bean.po.PaymentRecordPO;
import com.example.demo.config.WeChatPayProperties;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.PaymentRecordRepository;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.app.model.Amount;
import com.wechat.pay.java.service.payments.app.model.PrepayRequest;
import com.wechat.pay.java.service.payments.app.model.PrepayWithRequestPaymentResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class BillService {

    private final WeChatPayProperties properties;
    private final PaymentRecordRepository paymentRepo;
    private final OrderRepository orderRepo;

    // Optional: might be null if mock=true
    private final AppServiceExtension appService;

    @Transactional
    public Map<String, Object> prepay(String orderNo) {
        OrderPO order = orderRepo.findByOrderNo(orderNo)
                .orElseThrow(() -> new RuntimeException("订单不存在"));

        if (order.getPayStatus() == 1) {
            throw new RuntimeException("订单已支付");
        }

        // Generate our own trace ID if not exists, or just use orderNo as outTradeNo
        // Usually we might retry, so we use orderNo.
        // If we want to support multiple attempts for same order with different
        // outTradeNo, handle that.
        // For simplicity: outTradeNo = orderNo

        if (Boolean.TRUE.equals(properties.getMock())) {
            return mockPrepay(order);
        } else {
            return wechatPrepay(order);
        }
    }

    private Map<String, Object> mockPrepay(OrderPO order) {
        log.info("Mock Prepay for order: {}", order.getOrderNo());

        // Return dummy data that frontend can detect as "Mock"
        // Or return consistent format that automatically "succeeds" when used
        Map<String, Object> map = new HashMap<>();
        map.put("provider", "wxpay");
        map.put("mock", true);
        map.put("orderNo", order.getOrderNo());
        map.put("timestamp", String.valueOf(System.currentTimeMillis() / 1000));
        map.put("noncestr", UUID.randomUUID().toString().replace("-", ""));
        map.put("package", "Sign=WXPay"); // Dummy
        map.put("sign", "MOCK_SIGN");
        map.put("partnerId", "MOCK_PARTNER");
        map.put("prepayId", "MOCK_PREPAY_ID_" + order.getOrderNo());
        map.put("appid", "MOCK_APPID");
        return map;
    }

    private Map<String, Object> wechatPrepay(OrderPO order) {
        // Build Request
        PrepayRequest request = new PrepayRequest();
        Amount amount = new Amount();
        amount.setTotal(order.getPayAmount().multiply(new BigDecimal(100)).intValue()); // To Cent
        amount.setCurrency("CNY");

        request.setAmount(amount);
        request.setAppid(properties.getAppid());
        request.setMchid(properties.getMchid());
        request.setDescription("商品购买-" + order.getOrderNo());
        request.setNotifyUrl(properties.getNotifyUrl());
        request.setOutTradeNo(order.getOrderNo());

        // Call WeChat
        PrepayWithRequestPaymentResponse response = appService.prepayWithRequestPayment(request);

        // Save Record (Pending)
        createPendingRecord(order, "WECHAT");

        // Convert Response to Map for Frontend
        Map<String, Object> map = new HashMap<>();
        map.put("provider", "wxpay");
        map.put("appid", response.getAppid());
        map.put("partnerid", response.getPartnerId());
        map.put("prepayid", response.getPrepayId());
        map.put("package", response.getPackageVal());
        map.put("noncestr", response.getNonceStr());
        map.put("timestamp", response.getTimestamp());
        map.put("sign", response.getSign());

        return map;
    }

    private void createPendingRecord(OrderPO order, String type) {
        if (paymentRepo.findByOutTradeNo(order.getOrderNo()).isPresent()) {
            return;
        }
        PaymentRecordPO p = new PaymentRecordPO();
        p.setOrderNo(order.getOrderNo());
        p.setOutTradeNo(order.getOrderNo());
        p.setPayType(type);
        p.setPayAmount(order.getPayAmount());
        p.setPayStatus(0); // UNPAID
        p.setCreateTime(LocalDateTime.now());
        paymentRepo.save(p);
    }
}
