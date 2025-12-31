package com.example.auth.services;

import com.example.auth.bean.User;
import com.example.auth.oauth2.client.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

/**
 * 微信用户服务
 */
@Service
@Slf4j
public class WechatUserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 根据openid查找或创建用户
     *
     * @param openid    微信OpenID
     * @param unionid   微信UnionID (可选)
     * @param nickname  微信昵称 (可选)
     * @param avatarUrl 微信头像 (可选)
     * @return 用户实体和是否为新用户的标识
     */
    @Transactional
    public UserResult findOrCreateUserByOpenid(String openid, String unionid, String nickname, String avatarUrl) {
        Optional<User> existingUser = userRepository.findByWechatOpenid(openid);

        if (existingUser.isPresent()) {
            User user = existingUser.get();
            log.info("找到已存在的微信用户, openid: {}", openid);
            // 同步最新的微信资料
            boolean updated = false;
            if (nickname != null && !nickname.equals(user.getNickname())) {
                user.setNickname(nickname);
                updated = true;
            }
            if (avatarUrl != null && !avatarUrl.equals(user.getAvatarUrl())) {
                user.setAvatarUrl(avatarUrl);
                updated = true;
            }
            if (updated) {
                userRepository.save(user);
                log.info("更新用户微信资料, userId: {}", user.getId());
            }
            return new UserResult(user, false);
        }

        // 创建新用户
        User newUser = new User();
        newUser.setWechatOpenid(openid);
        newUser.setWechatUnionid(unionid);
        newUser.setNickname(nickname);
        newUser.setAvatarUrl(avatarUrl);

        // 生成随机用户名和密码 (微信登录不需要密码，但User实体要求非空)
        newUser.setUsername("wx_" + UUID.randomUUID().toString().substring(0, 8));
        newUser.setPassword(passwordEncoder.encode(UUID.randomUUID().toString()));

        // 设置默认值
        newUser.setEnabled(true);
        newUser.setAccountNonExpired(true);
        newUser.setAccountNonLocked(true);
        newUser.setCredentialsNonExpired(true);

        User savedUser = userRepository.save(newUser);
        log.info("创建新微信用户, openid: {}, userId: {}", openid, savedUser.getId());

        return new UserResult(savedUser, true);
    }

    /**
     * 用户结果封装类
     */
    public static class UserResult {
        private final User user;
        private final boolean isNewUser;

        public UserResult(User user, boolean isNewUser) {
            this.user = user;
            this.isNewUser = isNewUser;
        }

        public User getUser() {
            return user;
        }

        public boolean isNewUser() {
            return isNewUser;
        }
    }
}
