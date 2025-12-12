package com.example.order.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信支付配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.pay")
public class WechatPayProperties {

    /**
     * 小程序 AppID
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchid;

    /**
     * API v3 密钥
     */
    private String apiV3Key;

    /**
     * 商户API证书私钥路径
     */
    private String privateKeyPath;

    /**
     * 商户证书序列号
     */
    private String certificateSerialNumber;

    /**
     * 支付结果回调地址
     */
    private String notifyUrl;
}
