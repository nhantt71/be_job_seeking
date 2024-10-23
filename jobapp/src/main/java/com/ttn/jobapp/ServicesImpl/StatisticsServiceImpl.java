/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Dto.JobStatisticsDto;
import com.ttn.jobapp.Dto.StatisticsDto;
import com.ttn.jobapp.Repositories.JobCandidateRepository;
import com.ttn.jobapp.Repositories.JobRepository;
import com.ttn.jobapp.Services.StatisticsService;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private JobCandidateRepository jobCandidateRepository;

    @Override
    public List<StatisticsDto> getJobStatistics(int year, String periodType) {
        List<Object[]> jobStats;
        if ("MONTH".equals(periodType)) {
            jobStats = jobRepository.countJobsByMonth(year);
        } else {
            jobStats = jobRepository.countJobsByQuarter(year);
        }
        return jobStats.stream()
                .map(stat -> new StatisticsDto((int) (Number) stat[0], (Long) stat[1]))
                .collect(Collectors.toList());
    }

    @Override
    public List<StatisticsDto> getApplicationStatistics(int year, String periodType) {
        List<Object[]> applicationStats;
        if ("MONTH".equals(periodType)) {
            applicationStats = jobCandidateRepository.countApplicationsByMonth(year);
        } else {
            applicationStats = jobCandidateRepository.countApplicationsByQuarter(year);
        }
        return applicationStats.stream()
                .map(stat -> new StatisticsDto((int) (Number) stat[0], (Long) stat[1]))
                .collect(Collectors.toList());
    }
}
