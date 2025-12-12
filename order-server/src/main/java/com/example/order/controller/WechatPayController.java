package com.example.order.controller;

import com.example.common.core.entity.vo.Result;
import com.example.order.service.WechatPayService;
import com.wechat.pay.java.service.payments.jsapi.model.PrepayWithRequestPaymentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;

@RestController
@RequestMapping("/order/wechat")
@Tag(name = "微信支付接口")
public class WechatPayController {

    @Autowired
    private WechatPayService wechatPayService;

    @PostMapping("/pay/miniprogram")
    @Operation(summary = "小程序预支付下单")
    public Result<PrepayWithRequestPaymentResponse> createMiniProgramOrder(@RequestBody PayRequestDto request) {
        // 这里假设已经有了一个生成好的订单ID，实际业务中应该先创建订单，或者根据 orderId 查询订单详情
        // 为了演示方便，我们让前端直接传 tradeNo
        PrepayWithRequestPaymentResponse response = wechatPayService.prepay(
                request.getOpenid(),
                request.getTradeNo(),
                request.getAmount(),
                request.getDescription());
        Result<PrepayWithRequestPaymentResponse> result = new Result<>();
        result.setCode(Result.SUCCESSFUL_CODE);
        result.setMesg(Result.SUCCESSFUL_MESG);
        result.setData(response);
        result.setTime(java.time.Instant.now());
        return result;
    }

    @Data
    public static class PayRequestDto {
        private String openid; // 用户OpenID
        private String tradeNo; // 订单号
        private BigDecimal amount; // 金额
        private String description; // 商品描述
    }
}
