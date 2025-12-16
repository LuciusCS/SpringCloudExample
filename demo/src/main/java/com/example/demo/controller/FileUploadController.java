package com.example.demo.controller;

import com.example.demo.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api/files")
@RequiredArgsConstructor
@Slf4j
public class FileUploadController {

    private final MinioService minioService;

    @GetMapping("/test-connection")
    public ResponseEntity<String> testConnection() {
        try {
            boolean isConnected = minioService.testConnection();
            if (isConnected) {
                return ResponseEntity.ok("MinIO连接成功！");
            } else {
                return ResponseEntity.status(500).body("MinIO连接失败！");
            }
        } catch (Exception e) {
            log.error("连接测试失败", e);
            return ResponseEntity.internalServerError()
                    .body("连接测试失败: " + e.getMessage());
        }
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("文件不能为空");
            }

            String fileName = minioService.uploadFile(file);
            String presignedUrl = minioService.getPresignedUrl(fileName);

            return ResponseEntity.ok()
                    .body(presignedUrl);
        } catch (Exception e) {
            log.error("文件上传失败", e);
            return ResponseEntity.internalServerError()
                    .body("文件上传失败: " + e.getMessage());
        }
    }

    @GetMapping("/download/{fileName}")
    public ResponseEntity<byte[]> downloadFile(@PathVariable String fileName) {
        try {
            InputStream inputStream = minioService.downloadFile(fileName);
            byte[] bytes = inputStream.readAllBytes();
            inputStream.close();

            String encodedFileName = URLEncoder.encode(fileName, StandardCharsets.UTF_8.toString());

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + encodedFileName + "\"")
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(bytes);
        } catch (Exception e) {
            log.error("文件下载失败", e);
            return ResponseEntity.internalServerError()
                    .body(("文件下载失败: " + e.getMessage()).getBytes());
        }
    }

    @GetMapping("/url/{fileName}")
    public ResponseEntity<String> getPresignedUrl(@PathVariable String fileName) {
        try {
            String url = minioService.getPresignedUrl(fileName);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            log.error("获取预签名URL失败", e);
            return ResponseEntity.internalServerError()
                    .body("获取预签名URL失败: " + e.getMessage());
        }
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) {
        try {
            minioService.deleteFile(fileName);
            return ResponseEntity.ok("文件删除成功: " + fileName);
        } catch (Exception e) {
            log.error("文件删除失败", e);
            return ResponseEntity.internalServerError()
                    .body("文件删除失败: " + e.getMessage());
        }
    }


}