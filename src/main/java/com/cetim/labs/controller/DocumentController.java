package com.cetim.labs.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/document")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class DocumentController {

    @GetMapping("/os/{osId}/word")
    public ResponseEntity<byte[]> getWordDocument(@PathVariable int osId) {
        try {
            // Define the file path
            String fileName = "OS_" + osId + ".docx";
            Path filePath = Paths.get("Ordres-des-Service", fileName);
            
            // Read the file
            byte[] document = Files.readAllBytes(filePath);
            
            // Check if file exists
            if (document == null || document.length == 0) {
                return ResponseEntity.notFound().build();
            }

            // Set up headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDispositionFormData("attachment", fileName);
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document);
        } catch (IOException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/fichedessai/{fessaiID}/word")
    public ResponseEntity<byte[]> getFicheDessaiWordDocument(@PathVariable int fessaiID) {
        try {

            String fileName = "FicheDessai_" + fessaiID + ".docx";
            Path filePath = Paths.get("Fiches-Dessai", fileName);
            
            byte[] document = Files.readAllBytes(filePath);
            
            // Check if file exists
            if (document == null || document.length == 0) {
                return ResponseEntity.notFound().build();
            }
            
            
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document"));
            headers.setContentDispositionFormData("attachment", "Order_Service_" + fessaiID + ".docx");
            headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
            return ResponseEntity.ok()
                    .headers(headers)
                    .body(document);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
} 