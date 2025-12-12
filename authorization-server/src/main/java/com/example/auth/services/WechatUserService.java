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
     * @param openid  微信OpenID
     * @param unionid 微信UnionID (可选)
     * @return 用户实体和是否为新用户的标识
     */
    @Transactional
    public UserResult findOrCreateUserByOpenid(String openid, String unionid) {
        Optional<User> existingUser = userRepository.findByWechatOpenid(openid);

        if (existingUser.isPresent()) {
            log.info("找到已存在的微信用户, openid: {}", openid);
            return new UserResult(existingUser.get(), false);
        }

        // 创建新用户
        User newUser = new User();
        newUser.setWechatOpenid(openid);
        newUser.setWechatUnionid(unionid);

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
