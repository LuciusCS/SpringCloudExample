package com.example.auth.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

/**
 * 微信小程序登录请求
 */
@Data
@Schema(description = "微信小程序登录请求")
public class WechatLoginRequest {

    @Schema(description = "微信登录凭证code")
    private String code;
}
