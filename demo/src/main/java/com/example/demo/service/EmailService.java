package com.example.demo.service;

public interface EmailService {
    /**
     * 发送验证码
     * 
     * @param qqNumber QQ号
     */
    void sendVerificationCode(String qqNumber);

    /**
     * 验证验证码
     * 
     * @param qqNumber QQ号
     * @param code     验证码
     * @return 验证结果
     */
    boolean verifyCode(String qqNumber, String code);
}
