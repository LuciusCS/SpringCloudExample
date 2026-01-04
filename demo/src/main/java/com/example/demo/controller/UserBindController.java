package com.example.demo.controller;

import com.example.demo.bean.po.UserQQBindPO;
import com.example.demo.repository.UserQQBindRepository;
import com.example.demo.service.EmailService;
import com.example.demo.service.ImageAnalysisService;
import com.example.demo.service.MinioService;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user/bind")
public class UserBindController {

    @Resource
    private UserQQBindRepository userQQBindRepository;

    @Resource
    private EmailService emailService;

    @Resource
    private ImageAnalysisService imageAnalysisService;

    @Resource
    private MinioService minioService;

    @PostMapping("/qq")
    public ResponseEntity<String> bindQQ(@RequestParam Long userId,
            @RequestParam String qq,
            @RequestParam String code,
            @RequestParam("file") MultipartFile file) {
        // 1. Check if already bound
        Optional<UserQQBindPO> existing = userQQBindRepository.findByUserId(userId);
        if (existing.isPresent()) {
            return ResponseEntity.badRequest().body("该用户已绑定QQ");
        }

        // 2. Verify Code
        boolean isCodeValid = emailService.verifyCode(qq, code); // Assuming emailService handles qq email mapping
        if (!isCodeValid) {
            return ResponseEntity.badRequest().body("验证码错误或已过期");
        }

        try {
            // 3. Image Analysis
            // Create temp file
            File tempFile = File.createTempFile("bind_proof_", ".jpg");
            file.transferTo(tempFile);

            // 3a. Color Check
            boolean isSafeColor = imageAnalysisService.verifyImageColor(tempFile);
            if (!isSafeColor) {
                tempFile.delete();
                return ResponseEntity.badRequest().body("验证失败：请上传绿色安全背景的云黑码截图");
            }

            // 3b. OCR Check
            String ocrText = imageAnalysisService.extractQQNumber(tempFile);
            log.info("OCR Result for user {}: {}", userId, ocrText);

            // Clean OCR text: remove non-digit characters
            // String foundNumbers = ocrText.replaceAll("[^0-9]", "");
            // Simple check: does the OCR text contain the QQ number?
            if (!ocrText.contains(qq)) {
                tempFile.delete();
                return ResponseEntity.badRequest()
                        .body("验证失败：截图中的QQ号与输入的QQ号不一致 (识别结果: " + ocrText.replaceAll("\n", " ") + ")");
            }

            // 4. Upload to Minio
            String fileName = minioService.uploadFile(file); // upload the original multipart file

            // 5. Save to DB
            UserQQBindPO bindPO = new UserQQBindPO();
            bindPO.setUserId(userId);
            bindPO.setQq(qq);
            bindPO.setProofImage(fileName);
            userQQBindRepository.save(bindPO);

            // Cleanup
            tempFile.delete();

            return ResponseEntity.ok("绑定成功");

        } catch (Exception e) {
            log.error("Binding failed", e);
            return ResponseEntity.internalServerError().body("绑定失败: " + e.getMessage());
        }
    }

    @GetMapping("/info")
    public ResponseEntity<String> getBindInfo(@RequestParam Long userId) {
        Optional<UserQQBindPO> existing = userQQBindRepository.findByUserId(userId);
        return existing.map(userQQBindPO -> ResponseEntity.ok(userQQBindPO.getQq()))
                .orElseGet(() -> ResponseEntity.ok(null));
    }
}
