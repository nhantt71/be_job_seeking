/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Services.FileExtractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/file")
@CrossOrigin
public class ApiFileController {
    @Autowired
    private FileExtractionService fileExtractionService;

    @PostMapping("/extract-text")
    public ResponseEntity<String> extractTextFromFile(@RequestParam("file") MultipartFile file) {
        try {
            String content = fileExtractionService.extractTextWithOCR(file);
            return new ResponseEntity<>(content, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to extract content: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/detect-type")
    public ResponseEntity<String> detectFileType(@RequestParam("file") MultipartFile file) {
        try {
            String fileType = fileExtractionService.detectFileType(file);
            return new ResponseEntity<>(fileType, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to detect file type: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
