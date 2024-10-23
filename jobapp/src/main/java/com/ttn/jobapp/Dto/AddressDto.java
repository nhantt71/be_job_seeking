/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;


import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Getter
@Setter
public class AddressDto {

    @NotEmpty(message = "Detail is required")
    private String detail;

    @NotEmpty(message = "City is required")
    private String city;
    
    @NotEmpty(message = "Province is required")
    private String province;

    private Long companyId;
    
}