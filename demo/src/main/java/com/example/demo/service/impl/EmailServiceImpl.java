package com.example.demo.service.impl;

import com.example.demo.service.EmailService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class EmailServiceImpl implements EmailService {

    @Resource
    private JavaMailSender javaMailSender;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Value("${spring.mail.username}")
    private String from;

    private static final String EMAIL_CODE_PREFIX = "EMAIL_CODE:";

    @Override
    public void sendVerificationCode(String qqNumber) {
        String email = qqNumber + "@qq.com";
        String code = generateCode();

        // Save to Redis: 5 minutes expiration
        stringRedisTemplate.opsForValue().set(EMAIL_CODE_PREFIX + qqNumber, code, 5, TimeUnit.MINUTES);

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setTo(email);
        message.setSubject("验证码");
        message.setText("您的验证码是：" + code + "，有效期5分钟。");

        try {
            javaMailSender.send(message);
            log.info("验证码已发送至 {}", email);
        } catch (Exception e) {
            log.error("发送邮件失败", e);
            throw new RuntimeException("发送邮件失败");
        }
    }

    @Override
    public boolean verifyCode(String qqNumber, String code) {
        String cachedCode = stringRedisTemplate.opsForValue().get(EMAIL_CODE_PREFIX + qqNumber);
        return code != null && code.equals(cachedCode);
    }

    private String generateCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
}
