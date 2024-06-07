package com.example.controller.push;

public interface PushConstant {
    /**
     * OPPO推送server
     */
    String OPPO_SERVER_URL = "https://api.push.oppomobile.com/";

    /**
     * oppo获取token uri
     */
    String OPPO_TOKEN_URL = "/server/v1/auth";

    /**
     * 单推-通知栏消息推送
     */
    String OPPO_PUSH_URL = "/server/v1/message/notification/unicast";
    Integer ZERO = 0;

}
