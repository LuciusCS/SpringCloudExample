package com.example.auth.oidc;


import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * 自定义 OIDC 用户信息服务
 *
 * @author Ray Hao
 * @since 3.1.0
 */
@Service
@Slf4j
public class CustomOidcUserInfoService {

    private final UserDetailsService userDetailsService;

    public CustomOidcUserInfoService(UserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    public CustomOidcUserInfo loadUserByUsername(String username) {
        UserDetails userAuthInfo = null;
        try {
            userAuthInfo = userDetailsService.loadUserByUsername(username);
            if (userAuthInfo == null) {
                return null;
            }
            return new CustomOidcUserInfo(createUser(userAuthInfo));
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
            return null;
        }
    }

    private Map<String, Object> createUser(UserDetails userAuthInfo) {
        return CustomOidcUserInfo.customBuilder()
                .username(userAuthInfo.getUsername())
//                .nickname(userAuthInfo.getNickname())
//                .status(userAuthInfo.getStatus())
//                .phoneNumber(userAuthInfo.getMobile())
//                .email(userAuthInfo.getEmail())
//                .profile(userAuthInfo.getAvatar())
                .build()
                .getClaims();
    }

}
