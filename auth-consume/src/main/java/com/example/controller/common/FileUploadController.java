package com.example.controller.common;

import com.example.service.FileUploadService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/upload")
public class FileUploadController {

    @Autowired
    private FileUploadService fileUploadService;


    @PostMapping("/chunk")
    public void uploadChunk(

            @RequestParam("file") MultipartFile file,
            @RequestParam("filename") String filename,
            @RequestParam("chunkNumber") int chunkNumber
    ) throws Exception {


        fileUploadService.uploadChunk(filename, file.getInputStream(), file.getSize(), chunkNumber);
    }


    @PostMapping("/merge")
    public void mergeChunks(@RequestParam("filename") String filename,
                            @RequestParam("totalChunks") int totalChunks) throws Exception {
        fileUploadService.mergeChunks(filename, totalChunks);
    }

}
