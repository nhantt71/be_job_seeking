/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Services.SupabaseStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author PC
 */
@RestController
@RequestMapping("/upload")
public class ApiFileUploadController {
    
    @Autowired
    private SupabaseStorageService supabaseStorageService;
    
    public ApiFileUploadController(SupabaseStorageService storageService) {
        this.supabaseStorageService = storageService;
    }
    
    @PostMapping(value = "/avatar", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadAvatar(@RequestParam("file") MultipartFile file) throws Exception {
        return supabaseStorageService.uploadFile("avatar", file.getOriginalFilename(), file.getInputStream(), file.getContentType());
    }

    @PostMapping(value = "/cv", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public String uploadCV(@RequestParam("file") MultipartFile file) throws Exception {
        return supabaseStorageService.uploadFile("cv", file.getOriginalFilename(), file.getInputStream(), file.getContentType());
    }
    
}
