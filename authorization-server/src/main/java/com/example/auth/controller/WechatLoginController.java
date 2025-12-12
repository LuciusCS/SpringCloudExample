package com.example.auth.controller;

import cn.hutool.json.JSONObject;
import com.example.auth.bean.User;
import com.example.auth.bean.dto.WechatLoginRequest;
import com.example.auth.bean.dto.WechatLoginResponse;
import com.example.auth.services.JwtTokenService;
import com.example.auth.services.WechatMiniProgramService;
import com.example.auth.services.WechatUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 微信登录控制器
 */
@RestController
@RequestMapping("/auth/wechat")
@Tag(name = "微信登录接口")
@Slf4j
public class WechatLoginController {

    @Autowired
    private WechatMiniProgramService wechatMiniProgramService;

    @Autowired
    private WechatUserService wechatUserService;

    @Autowired
    private JwtTokenService jwtTokenService;

    @PostMapping("/miniprogram/login")
    @Operation(summary = "小程序登录", description = "通过微信小程序code登录，返回JWT token和openid")
    public WechatLoginResponse miniprogramLogin(@RequestBody WechatLoginRequest request) {
        log.info("收到小程序登录请求, code: {}", request.getCode());

        // 1. 调用微信API获取openid
        JSONObject sessionData = wechatMiniProgramService.getSessionByCode(request.getCode());
        String openid = sessionData.getStr("openid");
        String unionid = sessionData.getStr("unionid"); // 可能为null

        // 2. 查找或创建用户
        WechatUserService.UserResult userResult = wechatUserService.findOrCreateUserByOpenid(openid, unionid);
        User user = userResult.getUser();

        // 3. 生成JWT Token
        String token = jwtTokenService.generateToken(user);

        // 4. 返回响应
        return WechatLoginResponse.builder()
                .token(token)
                .openid(openid)
                .userId(user.getId())
                .isNewUser(userResult.isNewUser())
                .build();
    }
}
