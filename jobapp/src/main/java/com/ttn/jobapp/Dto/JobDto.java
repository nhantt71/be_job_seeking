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

    @NotEmpty(message = "Salary is required")
    private String salary;

    private String experience;

    private String detail;

    @NotNull(message = "Created date is required")
    private LocalDate createdDate;

    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @NotNull(message = "Company ID cannot be null")
    private Long companyId;
    
}