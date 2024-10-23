/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JobDto {
    
    private Long id;

    private String salary;

    private String experience;
    
    private String name;

    private String detail;

    private LocalDate createdDate;

    private LocalDate endDate;
    
    private String companyName;
    
    private String companyLogo;
    
    private String address;
    
    private Boolean enable;
    
    private Long companyId;
    
    private Long categoryId;
    
    private String categoryName;
    
    private Long recruiterId;
    
}