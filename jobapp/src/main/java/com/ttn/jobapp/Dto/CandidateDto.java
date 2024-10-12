/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Win11
 */
@Getter
@Setter
public class CandidateDto {

    @NotEmpty(message = "Full name is required")
    private String fullname;
    
    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;
    
    @NotNull(message = "Account is required")
    private Long accountId;
    
    private Boolean available;

}
