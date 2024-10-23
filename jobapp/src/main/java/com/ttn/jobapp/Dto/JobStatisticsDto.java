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
public class JobStatisticsDto {

     private int period;
    private long jobCount;
    private long applicationCount;

    public JobStatisticsDto(int period, long jobCount, long applicationCount) {
        this.period = period;
        this.jobCount = jobCount;
        this.applicationCount = applicationCount;
    }
}
