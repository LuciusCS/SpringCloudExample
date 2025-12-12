package com.example.order.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 微信支付配置类
 */
@Configuration
public class WechatPayConfig {

    @Autowired
    private WechatPayProperties wechatPayProperties;

    /**
     * 初始化 RSAAutoCertificateConfig
     * 使用 RSAAutoCertificateConfig 可以自动下载和更新微信支付平台证书
     */
    @Bean
    public RSAAutoCertificateConfig wechatPaySdkConfig() {
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(wechatPayProperties.getMchid())
                .privateKeyFromPath(wechatPayProperties.getPrivateKeyPath())
                .merchantSerialNumber(wechatPayProperties.getCertificateSerialNumber())
                .apiV3Key(wechatPayProperties.getApiV3Key())
                .build();
    }

    /**
     * JSAPI 支付 Service (适用于小程序支付)
     * 使用 Extension 版本可以生成用于调起支付的签名参数
     */
    @Bean
    public JsapiServiceExtension jsapiServiceExtension(Config config) {
        return new JsapiServiceExtension.Builder().config(config).build();
    }

    /**
     * 转账批次 Service (适用于提现功能)
     */
    @Bean
    public com.wechat.pay.java.service.transferbatch.TransferBatchService transferBatchService(Config config) {
        return new com.wechat.pay.java.service.transferbatch.TransferBatchService.Builder().config(config).build();
    }
}
