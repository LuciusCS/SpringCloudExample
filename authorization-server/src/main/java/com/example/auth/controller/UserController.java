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
        if (userRequest.getMobile() != null) {
            currentUser.setMobile(userRequest.getMobile());
        }
        if (userRequest.getQq() != null) {
            currentUser.setQq(userRequest.getQq());
        }
        if (userRequest.getSingleSignature() != null) {
            currentUser.setSingleSignature(userRequest.getSingleSignature());
        }
        if (userRequest.getDoubleSignature() != null) {
            currentUser.setDoubleSignature(userRequest.getDoubleSignature());
        }
        if (userRequest.getFourSignature() != null) {
            currentUser.setFourSignature(userRequest.getFourSignature());
        }

        userRepository.save(currentUser);
        return ResponseEntity.ok("更新成功");
    }

    @GetMapping("/profile/info")
    @Operation(summary = "获取当前用户信息")
    public ResponseEntity<User> getUserInfo(Authentication authentication) {
        Jwt jwt = (Jwt) authentication.getPrincipal();
        Long userId = jwt.getClaim("userId");
        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        // Hide password/sensitive info if needed, but @JsonIgnore on roles might be
        // enough,
        // password should be @JsonIgnore but isn't in the snippet I saw?
        // Checking User.java again... password doesn't have @JsonIgnore.
        // I should probably refrain from returning password.
        user.setPassword(null);

        return ResponseEntity.ok(user);
    }
}
