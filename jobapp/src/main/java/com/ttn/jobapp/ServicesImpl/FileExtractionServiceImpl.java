/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import org.apache.tika.metadata.Metadata;
import com.ttn.jobapp.Services.FileExtractionService;
import java.io.InputStream;
import org.apache.tika.Tika;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
@Service
public class FileExtractionServiceImpl implements FileExtractionService{

    @Override
    public String extractTextWithOCR(MultipartFile file) throws Exception {
        System.setProperty("TESSDATA_PREFIX", "D:\\Programs\\Tesseract-OCR\\");
        
        Parser parser = new AutoDetectParser();
        BodyContentHandler handler = new BodyContentHandler(Integer.MAX_VALUE);
        Metadata metadata = new Metadata();
        
        PDFParserConfig pdfConfig = new PDFParserConfig();
        TesseractOCRConfig config = new TesseractOCRConfig();
        ParseContext parseContext = new ParseContext();
       
        pdfConfig.setExtractInlineImages(true);
        
        parseContext.set(TesseractOCRConfig.class, config);
        parseContext.set(PDFParserConfig.class, pdfConfig);
        parseContext.set(Parser.class, parser);

        try (InputStream stream = file.getInputStream()) {
            parser.parse(stream, handler, metadata, parseContext);
            return handler.toString();
        } catch (Exception e) {
            throw new Exception("Error extracting content with OCR", e);
        }
    }

    @Override
    public String detectFileType(MultipartFile file) throws Exception{
        Tika tika = new Tika();
        return tika.detect(file.getInputStream());
    }
    
}
