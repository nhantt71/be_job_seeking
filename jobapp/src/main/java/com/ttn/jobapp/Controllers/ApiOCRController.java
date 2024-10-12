/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import java.io.InputStream;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.sax.BodyContentHandler;
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

    @PostMapping("/extract-text")
    public ResponseEntity<String> extractTextFromImage(@RequestPart("file") MultipartFile file) {
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
                return new ResponseEntity<>(extractedText, HttpStatus.OK);
            } else {
                return new ResponseEntity<>("Định dạng file không hỗ trợ OCR.", HttpStatus.BAD_REQUEST);
            }

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>("Có lỗi xảy ra trong quá trình xử lý hình ảnh.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

}
