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

    @Schema(description = "微信昵称")
    private String nickname;

    @Schema(description = "微信头像URL")
    private String avatar;
}
