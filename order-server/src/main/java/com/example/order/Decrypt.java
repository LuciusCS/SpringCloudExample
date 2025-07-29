package com.example.order;

import com.alibaba.druid.filter.config.ConfigTools;

public class Decrypt {
    public static void main(String[] args) throws Exception {
        // 加密后的密码
        String encryptedPassword = "CfJ1M6nnLRVaUG4NU1ublGdx8yy9QqMjXRZ7on7FnZ7c8Lj3nbLS9/yO5Uq0BBWEGj3em1OJb4fDYfwZaYUHoA==";

        // 私钥
        String privateKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALeNVVkDJC1N07hvR2b66NS0EO/nPKHnWqRx7+ftZrSog/5HmWFcjH99PyydcHNkPE+XBjkuN8k2gcyblQs6Yk8CAwEAAQ=="; // 全部的 config.decrypt.key

        // 解密
        String decryptedPassword = ConfigTools.decrypt(privateKey, encryptedPassword);
        System.out.println("明文密码：" + decryptedPassword);
    }
}
