/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
public interface FileExtractionService {
    
    String extractTextWithOCR(MultipartFile file) throws Exception;
    
    String detectFileType(MultipartFile file) throws Exception;
    
}
