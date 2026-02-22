package com.cetim.labs.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@RequestMapping("/api/upload")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class UploadController {

    private static final String UPLOAD_DIR = "src/main/resources/templates/";

    @PostMapping("/os")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            // Generate the file name as "template.docx"
            String fileName = "template.docx";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Delete the existing file if it exists
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            // Save the new file to the server
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @PostMapping("/fichedessai")
    public ResponseEntity<String> uploadfichedessaiFile(@RequestParam("file") MultipartFile file) {
        try {
            // Generate the file name as "template.docx"
            String fileName = "fichetemplate.docx";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Delete the existing file if it exists
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            // Save the new file to the server
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }

    @PostMapping("/test")
    public ResponseEntity<String> uploadTestiFile(@RequestParam("file") MultipartFile file , @RequestParam("testCode") String testcode) {
        try {
            // Generate the file name as "template.docx"
            String fileName = testcode + "_template.docx";
            Path filePath = Paths.get(UPLOAD_DIR + fileName);

            // Delete the existing file if it exists
            if (Files.exists(filePath)) {
                Files.delete(filePath);
            }

            // Save the new file to the server
            Files.write(filePath, file.getBytes());

            return ResponseEntity.ok("File uploaded successfully: " + fileName);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload file.");
        }
    }


}
