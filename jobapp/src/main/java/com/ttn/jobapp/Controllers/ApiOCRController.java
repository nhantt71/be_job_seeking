/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Services.SupabaseStorageService;
import com.ttn.jobapp.Services.TikaOcrService;
import java.io.InputStream;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/ocr")
@CrossOrigin
public class ApiOCRController {

    private final TikaOcrService tikaOcrService;
    private final SupabaseStorageService supabaseStorageService;

    public ApiOCRController(TikaOcrService tikaOcrService, SupabaseStorageService supabaseStorageService) {
        this.tikaOcrService = tikaOcrService;
        this.supabaseStorageService = supabaseStorageService;
    }

    @PostMapping("/extract/{candidateId}")
    public ResponseEntity<String> extractTextFromCv(
            @RequestParam("fileUrl") String supabaseUrl,
            @PathVariable Long candidateId) {

        try {
            // 1. Extract file path from Supabase URL
            String filePath = extractSupabasePath(supabaseUrl);

            // 2. Stream file directly from Supabase
            try (InputStream fileStream = supabaseStorageService.downloadFile("cv", filePath)) {

                // 3. Extract text using Tika-Tesseract
                String extractedText = tikaOcrService.extractText(fileStream);

                // 4. Escape string using Jackson
                ObjectMapper mapper = new ObjectMapper();
                String escapedJsonString = mapper.writeValueAsString(extractedText); // Includes escaping and adds double quotes

                // 5. Return it wrapped in a JSON object
                return ResponseEntity.ok(escapedJsonString);
            }
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("{\"error\":\"OCR processing failed: " + e.getMessage().replace("\"", "\\\"") + "\"}");
        }
    }

    private String extractSupabasePath(String url) {
        String prefix = "/storage/v1/object/public/cv/";
        int index = url.indexOf(prefix);
        if (index != -1) {
            return url.substring(index + prefix.length());
        }
        throw new IllegalArgumentException("Invalid Supabase URL format.");
    }

}
