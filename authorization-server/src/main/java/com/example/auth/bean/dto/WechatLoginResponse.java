package com.example.auth.bean.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

/**
 * 微信小程序登录响应
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "微信小程序登录响应")
public class WechatLoginResponse {

    @Schema(description = "JWT访问令牌")
    private String token;

    @Schema(description = "用户OpenID")
    private String openid;

    @Schema(description = "是否为新用户")
    private Boolean isNewUser;

    @Schema(description = "用户ID")
    private Long userId;

    @Schema(description = "用户权限列表")
    private Set<String> authorities;

    @Schema(description = "用户昵称")
    private String nickname;

    @Schema(description = "用户头像")
    private String avatar;
}
