/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;


import com.ttn.jobapp.Dto.StatisticsDto;
import java.util.List;

/**
 *
 * @author Win11
 */
public interface StatisticsService {
    List<StatisticsDto> getJobStatistics(int year, String periodType);
    List<StatisticsDto> getApplicationStatistics(int year, String periodType);
}
