/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author MyLaptop
 */
@Data
public class CategoryDto {
    
    private Long id;
    
    private String name;
    
    private String icon;
    
    private String phoneNumber;
    
    private String accountId;
    
    private int jobs;
    
    private MultipartFile imageFile;
    

}
