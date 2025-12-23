package com.example.demo.service;

import com.example.demo.config.MinioConfig;
import io.minio.*;
import io.minio.http.Method;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class MinioService {

    private final MinioClient minioClient;
    private final MinioConfig minioConfig;

    /**
     * Test connection to MinIO server
     */
    public boolean testConnection() {
        try {
            minioClient.listBuckets();
            return true;
        } catch (Exception e) {
            log.error("MinIO connection failed: {}", e.getMessage());
            return false;
        }
    }

    /**
     * Upload file to MinIO
     */
    public String uploadFile(MultipartFile file) throws Exception {
        String bucketName = minioConfig.getBucketName();
        String originalFilename = file.getOriginalFilename();
        String objectName = System.currentTimeMillis() + "_" + originalFilename;

        // Ensure bucket exists
        if (!minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build())) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            log.info("Bucket '{}' created.", bucketName);
        }

        // Upload
        try (InputStream inputStream = file.getInputStream()) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(inputStream, file.getSize(), -1) // 5MB part size
                            .contentType(file.getContentType())
                            .build());
        }

        log.info("File uploaded successfully: {}", objectName);
        return objectName;
    }

    /**
     * Get file download stream
     */
    public InputStream downloadFile(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .build());
    }

    /**
     * Get presigned URL for file access
     */
    public String getPresignedUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Method.GET)
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .expiry(7, TimeUnit.DAYS)
                        .build());
    }

    /**
     * Delete file
     */
    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(minioConfig.getBucketName())
                        .object(objectName)
                        .build());
        log.info("File deleted: {}", objectName);
    }

    /**
     * Get public URL for file access
     */
    public String getPublicUrl(String objectName) {
        if (objectName == null || objectName.isEmpty()) {
            return null;
        }
        String endpoint = minioConfig.getEndpoint();
        if (endpoint.endsWith("/")) {
            endpoint = endpoint.substring(0, endpoint.length() - 1);
        }
        return endpoint + "/" + minioConfig.getBucketName() + "/" + objectName;
    }

    @jakarta.annotation.PostConstruct
    public void init() {
        String bucketName = minioConfig.getBucketName();
        try {
            boolean found = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!found) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
                log.info("Bucket '{}' created.", bucketName);
            }

            // Set bucket policy to public read
            String policy = "{\n" +
                    "  \"Version\": \"2012-10-17\",\n" +
                    "  \"Statement\": [\n" +
                    "    {\n" +
                    "      \"Effect\": \"Allow\",\n" +
                    "      \"Principal\": {\n" +
                    "        \"AWS\": [\n" +
                    "          \"*\"\n" +
                    "        ]\n" +
                    "      },\n" +
                    "      \"Action\": [\n" +
                    "        \"s3:GetObject\"\n" +
                    "      ],\n" +
                    "      \"Resource\": [\n" +
                    "        \"arn:aws:s3:::" + bucketName + "/*\"\n" +
                    "      ]\n" +
                    "    }\n" +
                    "  ]\n" +
                    "}";

            minioClient.setBucketPolicy(
                    SetBucketPolicyArgs.builder().bucket(bucketName).config(policy).build());
            log.info("Bucket policy set to public read for '{}'", bucketName);

        } catch (Exception e) {
            log.error("Error initializing MinIO bucket: {}", e.getMessage());
        }
    }
}