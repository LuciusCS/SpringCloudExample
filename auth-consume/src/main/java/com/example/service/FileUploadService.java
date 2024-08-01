package com.example.service;


import io.minio.MinioClient;
import io.minio.UploadObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;


///用于文件上传
@Service
public class FileUploadService {

    @Autowired
    private MinioClient minioClient;

    @Value("${minio.bucket-name}")
    private String bucketName;


    private Path tempDir;

    public FileUploadService() throws Exception {
        // 创建一次临时目录，用于存储所有分块
        tempDir = Files.createTempDirectory("chunks");
    }
     public  void uploadChunk(String fileName, InputStream chunkInputStream, long chunkSize, int chunkNumber) throws Exception {


         // 用于临时保存分块
//         Path tempDir = Files.createTempDirectory("chunks");
         Path chunkFile = tempDir.resolve(fileName + ".part" + chunkNumber);

         // 确保父目录存在
//         Files.createDirectories(chunkFile.getParent());

         ///用于临时保存分块
//         Path tempDir =Files.createTempDirectory("chunks");
//         Path chunkFile=tempDir.resolve(fileName+".part"+chunkNumber);

         Files.copy(chunkInputStream,chunkFile, StandardCopyOption.REPLACE_EXISTING);
         System.out.println("Chunk " + chunkNumber + " saved to " + chunkFile.toString());
         // 检查是否所有分块都已上传
         // 如果是，则合并所有分块并上传到MinIO
         // 否则，只存储当前分块


     }

     ///合并所有的块上传值minio
    public  void mergeChunks(String fileName,int totalChunks)throws Exception {

        ///这里最好通过md5校验文件的完整性
//         Path tempDir=Files.createTempDirectory("chunks");
         Path finalFile=tempDir.resolve(fileName);

         try(OutputStream outputStream=Files.newOutputStream(finalFile)){
             for (int i = 0; i < totalChunks; i++) {
                 Path chunkFile = tempDir.resolve(fileName + ".part" + i);
                 Files.copy(chunkFile, outputStream);
                 Files.delete(chunkFile); // 删除临时分块文件
             }
         }

         ///上传合并后的文件到minio
         minioClient.uploadObject(
                 UploadObjectArgs.builder()
                         .bucket(bucketName)
                         .object(finalFile.toString())
                         .filename(fileName)
                         .build()
         );
         Files.delete(finalFile);
    }

}
