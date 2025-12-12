package com.example.auth.services;

import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.example.auth.config.WechatMiniProgramProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 微信小程序服务
 */
@Service
@Slf4j
public class WechatMiniProgramService {

    @Autowired
    private WechatMiniProgramProperties wechatProperties;

    private static final String CODE2SESSION_URL = "https://api.weixin.qq.com/sns/jscode2session";

    /**
     * 通过code获取openid和session_key
     *
     * @param code 小程序登录凭证
     * @return 包含openid、session_key、unionid的JSON对象
     */
    public JSONObject getSessionByCode(String code) {
        String url = String.format("%s?appid=%s&secret=%s&js_code=%s&grant_type=authorization_code",
                CODE2SESSION_URL,
                wechatProperties.getAppid(),
                wechatProperties.getSecret(),
                code);

        log.info("调用微信code2session接口, code: {}", code);

        String response = HttpUtil.get(url);
        JSONObject jsonObject = JSONUtil.parseObj(response);

        // 检查是否有错误
        if (jsonObject.containsKey("errcode")) {
            Integer errcode = jsonObject.getInt("errcode");
            if (errcode != 0) {
                String errmsg = jsonObject.getStr("errmsg");
                log.error("微信code2session失败, errcode: {}, errmsg: {}", errcode, errmsg);
                throw new RuntimeException("微信登录失败: " + errmsg);
            }
        }

        log.info("微信code2session成功, openid: {}", jsonObject.getStr("openid"));
        return jsonObject;
    }
}
