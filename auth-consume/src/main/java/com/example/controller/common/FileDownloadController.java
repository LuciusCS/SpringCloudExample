package com.example.controller.common;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.StatObjectArgs;
import io.minio.StatObjectResponse;
import io.minio.MinioClient;
import io.minio.StatObjectResponse;
import io.minio.errors.MinioException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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

    ///用于表示实现文件分块下载
    @GetMapping("/download/chunk")
    public ResponseEntity<InputStreamResource> downloadFile(@RequestParam String bucket,
                                                            @RequestParam String object,
                                                            @RequestHeader(value = "Range", required = false) String rangeHeader) throws Exception {
        long rangeStart = 0;
        long rangeEnd = Long.MAX_VALUE;

        if (rangeHeader != null && rangeHeader.startsWith("bytes=")) {
            String[] ranges = rangeHeader.substring(6).split("-");
            if (ranges.length > 0) {
                rangeStart = Long.parseLong(ranges[0]);
                if (ranges.length > 1) {
                    rangeEnd = Long.parseLong(ranges[1]);
                }
            }
        }

        ///从minio中获取数据
        InputStream inputStream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucket)
                        .object(object)
                        .offset(rangeStart)
                        .length(rangeEnd - rangeStart + 1)
                        .build()
        );

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_OCTET_STREAM_VALUE)
                .header(HttpHeaders.ACCEPT_RANGES, "bytes")
                .header(HttpHeaders.CONTENT_RANGE, "bytes " + rangeStart + "-" + rangeEnd + "/*")
                .body(new InputStreamResource(inputStream));
    }


    @RequestMapping(value = "/download/chunk",method = RequestMethod.HEAD)
    public ResponseEntity<Void> headFile(
            @RequestParam String bucket,
            @RequestParam String object) {
        try {
            StatObjectResponse stat = minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucket)
                            .object(object)
                            .build());

            long fileSize = stat.size();

            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.setContentLength(fileSize);
            responseHeaders.add(HttpHeaders.ACCEPT_RANGES, "bytes");

            return new ResponseEntity<>(responseHeaders, HttpStatus.OK);
        } catch (MinioException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        } catch (InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}
