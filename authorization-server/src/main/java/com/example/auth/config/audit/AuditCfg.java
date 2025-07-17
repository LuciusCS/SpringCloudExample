package com.example.auth.config.audit;

import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@EnableJpaAuditing
@Configuration
public class AuditCfg {

    @Bean
    public AuditorAware<String> auditorProvider() {
        // 从 Spring Security 获取当前的认证信息
        return () -> {
            // 从 Spring Security 获取当前的认证信息
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()) {
                // 获取当前用户的用户名，这里改成用户id
                return Optional.of(authentication.getName());
            } else {
                // 如果未认证或不存在，返回默认用户名
                return Optional.of("anonymousUser");
            }
        };
    }
}
