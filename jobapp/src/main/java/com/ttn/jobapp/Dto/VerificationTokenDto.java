/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author Win11
 */
@Data
public class VerificationTokenDto {
    private String token;
    private LocalDateTime expiryDate;
    private Long companyId;
}
