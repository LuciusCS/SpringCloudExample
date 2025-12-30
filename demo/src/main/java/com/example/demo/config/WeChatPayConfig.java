package com.example.demo.config;

import com.wechat.pay.java.core.Config;
import com.wechat.pay.java.core.RSAAutoCertificateConfig;
import com.wechat.pay.java.service.payments.app.AppServiceExtension;
import com.wechat.pay.java.service.payments.jsapi.JsapiServiceExtension;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.File;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class WeChatPayConfig {

    private final WeChatPayProperties properties;

    @Bean
    @ConditionalOnProperty(name = "wechat.pay.mock", havingValue = "false", matchIfMissing = false)
    public Config wechatConfig() {
        // Only load if not in mock mode (or lenient load)
        // If privateKeyPath is missing in dev, this might fail.
        // We add a check or try-catch if needed, but standard way is to fail fast.

        // Ensure file exists to avoid confused errors
        // If we are just testing locally without certs, we might rely on Mock
        return new RSAAutoCertificateConfig.Builder()
                .merchantId(properties.getMchid())
                .privateKeyFromPath(properties.getPrivateKeyPath())
                .merchantSerialNumber(properties.getMerchantSerialNumber())
                .apiV3Key(properties.getApiV3Key())
                .build();
    }

    /**
     * Service for App Payment (Native App) outputting prepay_id
     */
    @Bean
    @ConditionalOnProperty(name = "wechat.pay.mock", havingValue = "false", matchIfMissing = false)
    public AppServiceExtension appServiceExtension(Config config) {
        return new AppServiceExtension.Builder().config(config).build();
    }

    /**
     * Service for JSAPI Payment (MiniProgram) outputting prepay_id
     */
    @Bean
    @ConditionalOnProperty(name = "wechat.pay.mock", havingValue = "false", matchIfMissing = false)
    public JsapiServiceExtension jsapiServiceExtension(Config config) {
        return new JsapiServiceExtension.Builder().config(config).build();
    }
}
