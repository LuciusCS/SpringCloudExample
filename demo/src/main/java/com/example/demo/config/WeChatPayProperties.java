package com.example.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "wechat.pay")
public class WeChatPayProperties {

    /**
     * 商户号
     */
    private String mchid;

    /**
     * 商户证书序列号
     */
    private String merchantSerialNumber;

    /**
     * 商户私钥路径
     */
    private String privateKeyPath;

    /**
     * API V3 密钥
     */
    private String apiV3Key;

    /**
     * AppID
     */
    private String appid;

    /**
     * 回调地址
     */
    private String notifyUrl;

    /**
     * 是否开启 Mock 模式
     */
    private Boolean mock = false;
}
