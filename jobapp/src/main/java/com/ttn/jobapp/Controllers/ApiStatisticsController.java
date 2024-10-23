/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Controllers;

import com.ttn.jobapp.Dto.StatisticsDto;
import com.ttn.jobapp.Services.StatisticsService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Win11
 */
@RestController
@RequestMapping("api/statistics")
public class ApiStatisticsController {

    @Autowired
    private StatisticsService statisticsService;

    @GetMapping("/jobs")
    public List<StatisticsDto> getJobStatistics(
            @RequestParam int year,
            @RequestParam String periodType) {
        return statisticsService.getJobStatistics(year, periodType);
    }

    @GetMapping("/applications")
    public List<StatisticsDto> getApplicationStatistics(
            @RequestParam int year,
            @RequestParam String periodType) {
        return statisticsService.getApplicationStatistics(year, periodType);
    }
}