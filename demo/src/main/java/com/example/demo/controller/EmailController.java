package com.example.demo.controller;

import com.example.demo.service.EmailService;
import jakarta.annotation.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/email")
public class EmailController {

    @Resource
    private EmailService emailService;

    @PostMapping("/send")
    public ResponseEntity<String> sendVerificationCode(@RequestParam String qqNumber) {
        try {
            emailService.sendVerificationCode(qqNumber);
            return ResponseEntity.ok("验证码发送成功");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("发送失败: " + e.getMessage());
        }
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyCode(@RequestParam String qqNumber, @RequestParam String code) {
        boolean isValid = emailService.verifyCode(qqNumber, code);
        if (isValid) {
            return ResponseEntity.ok("验证成功");
        } else {
            return ResponseEntity.badRequest().body("验证失败：验证码错误或已过期");
        }
    }
}
