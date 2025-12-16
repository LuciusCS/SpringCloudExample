package com.example.demo.controller;

import com.example.demo.service.PayCallbackService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pay/callback")
@RequiredArgsConstructor
public class PayCallbackController {

    private final PayCallbackService callbackService;

    @PostMapping("/wechat")
    public String wechatCallback(@RequestBody String notifyBody) {

        // 1️⃣ 验签（示例，实际用微信 SDK）
        WechatNotify notify = WechatNotifyParser.parse(notifyBody);

        if (!notify.isSuccess()) {
            return "FAIL";
        }

        // 2️⃣ 处理业务
        callbackService.handleWechatPaySuccess(notify, notifyBody);

        return "SUCCESS";
    }
}
