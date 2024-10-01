/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Win11
 */
@Getter
@Setter
public class CompanyDto {
    private Long id;

    @NotEmpty(message = "Name is required")
    private String name;

    @Email(message = "Email should be valid")
    @NotEmpty(message = "Email is required")
    private String email;

    private List<AddressDto> address;
    
    private String logo;

    private String phoneNumber;

    private String website;

    private String information;

    private List<JobDto> jobs;
    
    private Long employerId;
    
    private int jobAmount;
}