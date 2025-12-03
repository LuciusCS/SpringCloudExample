package com.example.order;

import com.alibaba.druid.filter.config.ConfigTools;

public class Decrypt {
    public static void main(String[] args) throws Exception {
        // 加密后的密码
        String encryptedPassword = "J5qfFbHeGUTzW5kIS/WH6uJAZhfyzVdgKXBSppwxQuv4cUe+3MEXlqgH8f7KOikUvp2BPmhUx1B1ReW33STYMQ==";

        // 私钥
        String privateKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALbpRE1SXrldIPWYy70MiyzLWxMKQSQi0nL6ZSE5uyiA+72AC0CV3+Vr/xdjx/5E/JT/xuGchmyr3dUfHjZUrOMCAwEAAQ=="; // 全部的 config.decrypt.key

        // 解密
        String decryptedPassword = ConfigTools.decrypt(privateKey, encryptedPassword);
        System.out.println("明文密码：" + decryptedPassword);
    }
}
