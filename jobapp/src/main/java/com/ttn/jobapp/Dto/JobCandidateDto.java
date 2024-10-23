/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Dto;

import java.time.LocalDate;
import java.time.LocalDateTime;
import lombok.Data;

/**
 *
 * @author MyLaptop
 */
@Data
public class JobCandidateDto {
    
    private Long candidateId;
    
    private Long jobId;
    
    private Boolean applied;
    
    private Boolean saved;
    
    private LocalDateTime savedAt;
    
    private LocalDate appliedAt;
}
