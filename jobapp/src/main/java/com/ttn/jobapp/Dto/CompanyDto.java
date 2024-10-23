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
import org.springframework.web.multipart.MultipartFile;

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

    @NotEmpty(message = "Email is required")
    private String email;

    private List<AddressDto> address;
    
    private String logo;

    private String phoneNumber;

    private String website;

    private String information;

    private List<JobDto> jobs;
    
    private Long recruiterId;
    
    private int jobAmount;
    
    private String addressDetail;
    
    private String city;
    
    private String province;
    
    private Long addressId;
    
    private Long createdRecruiterId;
    
    private Boolean verified;
    
    private MultipartFile imageFile;
    
}