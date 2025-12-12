package com.example.auth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * 微信小程序配置属性
 */
@Data
@Component
@ConfigurationProperties(prefix = "wechat.miniprogram")
public class WechatMiniProgramProperties {

    /**
     * 小程序 AppID
     */
    private String appid;

    /**
     * 小程序 AppSecret
     */
    private String secret;
}
