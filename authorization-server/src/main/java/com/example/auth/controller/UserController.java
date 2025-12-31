package com.example.auth.controller;

import com.example.auth.bean.User;
import com.example.auth.feign.FileUploadClient;
import com.example.auth.oauth2.client.repository.UserRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/auth/user")
@Tag(name = "用户核心接口")
@RequiredArgsConstructor
@Slf4j
public class UserController {

    private final FileUploadClient fileUploadClient;
    private final UserRepository userRepository;

    @PostMapping("/avatar/upload")
    @Operation(summary = "上传头像")
    public ResponseEntity<String> uploadAvatar(@RequestParam("file") MultipartFile file,
            Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userId = jwt.getClaim("userId");
        log.info("开始上传头像, 用户ID: {}", userId);

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        try {
            String fileName = fileUploadClient.uploadFile(file);
            log.info("头像上传成功, 文件名: {}", fileName);

            currentUser.setAvatarUrl(fileName);
            userRepository.save(currentUser);

            return ResponseEntity.ok(fileName);
        } catch (Exception e) {
            log.error("上传头像失败", e);
            return ResponseEntity.internalServerError().body("上传头像失败: " + e.getMessage());
        }
    }

    @PostMapping("/profile/update")
    @Operation(summary = "更新用户信息")
    public ResponseEntity<String> updateProfile(@RequestBody User userRequest, Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userId = jwt.getClaim("userId");

        User currentUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        if (userRequest.getNickname() != null) {
            currentUser.setNickname(userRequest.getNickname());
        }
        userRepository.save(currentUser);
        return ResponseEntity.ok("更新成功");
    }
}
