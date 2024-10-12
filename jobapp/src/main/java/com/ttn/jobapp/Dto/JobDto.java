/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class JobDto {
    
    private Long id;

    @NotEmpty(message = "Salary is required")
    private String salary;

    private String experience;
    
    private String name;

    private String detail;

    @NotNull(message = "Created date is required")
    private LocalDate createdDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    private String companyName;
    
    private String companyLogo;
    
    private String address;
    
    private Boolean enable = false;
    
    @NotNull(message = "Company ID cannot be null")
    private Long companyId;
    
    private Long categoryId;
    
    private String categoryName;
    
}