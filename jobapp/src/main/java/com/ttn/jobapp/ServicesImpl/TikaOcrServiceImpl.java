/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Services.TikaOcrService;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.ocr.TesseractOCRConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xml.sax.ContentHandler;

/**
 *
 * @author PC
 */
@Service
@RequiredArgsConstructor
public class TikaOcrServiceImpl implements TikaOcrService {

    @Autowired
    private Parser parser;

    @Autowired
    private TesseractOCRConfig tesseractConfig;

    @Value("${tika.ocr.tesseract.path}")
    private String tesseractPath;

    @Value("${tika.ocr.tesseract.datapath}")
    private String tessdataPath;

    @Value("${tika.ocr.language}")
    private String language;

    @PostConstruct
    public void init() {
        this.parser = new AutoDetectParser();
        this.tesseractConfig = new TesseractOCRConfig();

        // Configure Tesseract
        tesseractConfig.setLanguage(language);
        tesseractConfig.setPageSegMode("1"); // Auto page segmentation
        System.setProperty("TESSDATA_PREFIX", tessdataPath);
    }

    @Override
    public String extractText(InputStream fileStream) throws Exception {
        ContentHandler handler = new BodyContentHandler(-1); // No size limit
        Metadata metadata = new Metadata();
        ParseContext context = new ParseContext();

        context.set(TesseractOCRConfig.class, tesseractConfig);
        context.set(Parser.class, parser);

        try {
            parser.parse(fileStream, handler, metadata, context);
            return handler.toString();
        } finally {
            fileStream.close();
        }
    }
}
