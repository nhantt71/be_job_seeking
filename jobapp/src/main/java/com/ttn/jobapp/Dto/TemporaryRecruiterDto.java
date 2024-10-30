/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import lombok.Data;

/**
 *
 * @author Win11
 */
@Data
public class TemporaryRecruiterDto {
    
    private Long companyId;
    
    private Long recruiterId;
    
    private String avatar;
    
    private String fullname;
    
    private String email;
    
    private String phoneNumber;
    
}
