package com.arbek.controllers;

import com.arbek.service.FileService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("/file/")
@CrossOrigin(origins = "*")
public class FileController {
    private final FileService fileService;


    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @Value("${project.poster}")
    private String path;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestPart MultipartFile file) throws IOException{
        String uploadFileName = fileService.uploadFile(path, file);
        return ResponseEntity.ok("File uploaded : " + uploadFileName);
    }

    @GetMapping("/{fileName}")
    public void ServeFileHandler(@PathVariable String fileName, HttpServletResponse response) throws IOException {
        InputStream resourceFile =  fileService.getResourceFile(path, fileName);
            response.setContentType(MediaType.IMAGE_JPEG_VALUE);
        StreamUtils.copy(resourceFile, response.getOutputStream());
    }

    @DeleteMapping("/{fileName}")
    public ResponseEntity<String> deleteFile(@PathVariable String fileName) throws IOException {
        boolean deleted = fileService.deleteFile(path, fileName);
        if (deleted) {
            return ResponseEntity.ok("File deleted: " + fileName);
        } else {
            return ResponseEntity.badRequest().body("Failed to delete file: " + fileName);
        }
    }

}
