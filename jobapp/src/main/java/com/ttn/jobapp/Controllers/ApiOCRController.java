/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Pojo.MongoExtractCV;
import com.ttn.jobapp.Services.MongoExtractCVService;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.xml.sax.ContentHandler;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("/api/ocr")
@CrossOrigin
public class ApiOCRController {
    
    @Autowired
    private MongoExtractCVService cvExtractionService;

    @PostMapping("/extract-text/{candidateId}")
    public ResponseEntity<String> extractTextFromImage(@RequestPart("file") MultipartFile file, @PathVariable("candidateId") Long candidateId) {
        try (InputStream inputStream = file.getInputStream()) {
            String fileType = file.getContentType();
            System.out.println("Processing file of type: " + fileType);

            ContentHandler handler = new BodyContentHandler();
            Metadata metadata = new Metadata();
            ParseContext parseContext = new ParseContext();

            if (fileType != null && (fileType.startsWith("image/") || fileType.equals("application/pdf"))) {
                TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
                ocrConfig.setLanguage("eng");

                System.setProperty("TESSDATA_PREFIX", "C:\\Program Files\\Tesseract-OCR\\tessdata");

                parseContext.set(TesseractOCRConfig.class, ocrConfig);

                AutoDetectParser parser = new AutoDetectParser();
                parser.parse(inputStream, handler, metadata, parseContext);

                String extractedText = handler.toString();
                
                MongoExtractCV savedCV = cvExtractionService.extractCVInformation(extractedText, candidateId);
                
                return new ResponseEntity<>(savedCV.getId(), HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Định dạng file không hỗ trợ OCR.", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Có lỗi xảy ra trong quá trình xử lý hình ảnh.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @PostMapping("/extract-text-from-url/{candidateId}")
    public ResponseEntity<String> extractTextFromImageUrl(@RequestParam("imageUrl") String imageUrl,
            @PathVariable("candidateId") Long candidateId) {
        try {
            URL url = new URL(imageUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                return new ResponseEntity<>("Không thể tải hình ảnh từ URL.", HttpStatus.BAD_REQUEST);
            }

            try (InputStream inputStream = new BufferedInputStream(connection.getInputStream())) {
                ContentHandler handler = new BodyContentHandler();
                Metadata metadata = new Metadata();
                ParseContext parseContext = new ParseContext();

                TesseractOCRConfig ocrConfig = new TesseractOCRConfig();
                ocrConfig.setLanguage("eng");

                System.setProperty("TESSDATA_PREFIX", "C:\\Program Files\\Tesseract-OCR\\tessdata");

                parseContext.set(TesseractOCRConfig.class, ocrConfig);

                AutoDetectParser parser = new AutoDetectParser();
                parser.parse(inputStream, handler, metadata, parseContext);

                String extractedText = handler.toString();
                
                MongoExtractCV savedCV = cvExtractionService.extractCVInformation(extractedText, candidateId);
                
                return new ResponseEntity<>(savedCV.getId(), HttpStatus.OK);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Có lỗi xảy ra trong quá trình xử lý hình ảnh từ URL.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    
    @DeleteMapping("/delete-by-candidate-id/{candidateId}")
    public ResponseEntity<String> deleteByCandidateId(@PathVariable Long candidateId) {
        try {
            this.cvExtractionService.deleteByCandidateId(candidateId);
            return ResponseEntity.ok("Document with candidateId " + candidateId + " has been deleted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body("Error deleting document: " + e.getMessage());
        }
    }

}
