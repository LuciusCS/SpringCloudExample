package com.example.controller.common;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.MinioClient;
import io.minio.StatObjectResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRange;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.util.List;


///用于下载断点续传
@Controller
public class FileDownloadController {
    @Autowired
    private MinioClient minioClient;

    @GetMapping("/download")
    public ResponseEntity<Resource> downloadFile(
            @RequestParam String bucketName,
            @RequestParam String objectName,
            @RequestHeader HttpHeaders headers

    )throws  Exception{

//        StatObjectResponse stat=minioClient.statObject(bucketName,objectName);
        StatObjectResponse stat = minioClient.statObject(StatObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
        List<HttpRange> ranges = headers.getRange();
        long fileLength = stat.size();
        HttpHeaders responseHeaders = new HttpHeaders();
        if (ranges.isEmpty()) {
            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .build());

            InputStreamResource resource = new InputStreamResource(inputStream);
            responseHeaders.setContentLength(fileLength);
            return ResponseEntity.ok()
                    .headers(responseHeaders)
                    .body(resource);
        } else {
            HttpRange range = ranges.get(0);
            long start = range.getRangeStart(fileLength);
            long end = range.getRangeEnd(fileLength);

            if (start >= fileLength) {
                responseHeaders.set(HttpHeaders.CONTENT_RANGE, "bytes */" + fileLength);
                return ResponseEntity.status(HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE)
                        .headers(responseHeaders)
                        .body(null);
            }

            if (end >= fileLength) {
                end = fileLength - 1;
            }

            long contentLength = end - start + 1;

            InputStream inputStream = minioClient.getObject(GetObjectArgs.builder()
                    .bucket(bucketName)
                    .object(objectName)
                    .offset(start)
                    .length(contentLength)
                    .build());

            InputStreamResource resource = new InputStreamResource(inputStream);
            responseHeaders.set(HttpHeaders.CONTENT_RANGE, "bytes " + start + "-" + end + "/" + fileLength);
            responseHeaders.setContentLength(contentLength);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                    .headers(responseHeaders)
                    .body(resource);
        }

    }

}
