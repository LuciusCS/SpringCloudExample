package com.example.order.service;

import com.example.order.config.WechatPayProperties;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.model.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

/**
 * 微信支付服务
 */
@Service
@Slf4j
public class WechatPayService {

    @Autowired
    private WechatPayProperties wechatPayProperties;

    @Autowired
    private JsapiServiceExtension jsapiServiceExtension;

    /**
     * 小程序预支付下单
     *
     * @param openid      用户的 OpenID
     * @param tradeNo     商户订单号
     * @param amount      金额 (元)
     * @param description 订单描述
     * @return 调起支付所需的参数对象
     */
    public PrepayWithRequestPaymentResponse prepay(String openid, String tradeNo, BigDecimal amount,
            String description) {
        PrepayRequest request = new PrepayRequest();

        // 1. 设置应用ID和商户号
        request.setAppid(wechatPayProperties.getAppid());
        request.setMchid(wechatPayProperties.getMchid());

        // 2. 设置订单信息
        request.setDescription(description);
        request.setOutTradeNo(tradeNo);
        request.setNotifyUrl(wechatPayProperties.getNotifyUrl());

        // 3. 设置金额 (转为分)
        Amount amountObj = new Amount();
        amountObj.setTotal(amount.multiply(new BigDecimal("100")).intValue());
        amountObj.setCurrency("CNY");
        request.setAmount(amountObj);

        // 4. 设置支付者 (小程序需要 OpenID)
        Payer payer = new Payer();
        payer.setOpenid(openid);
        request.setPayer(payer);

        log.info("发起微信支付下单, tradeNo: {}, openid: {}", tradeNo, openid);

        // 5. 调用接口下单并生成支付参数
        // prepayWithRequestPayment 会同时完成下单和签名生成，返回直接可用于类似 wx.requestPayment 的参数
        return jsapiServiceExtension.prepayWithRequestPayment(request);
    }
}
