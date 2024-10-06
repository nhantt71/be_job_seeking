/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Dto.JobDto;
import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Repositories.JobRepository;
import com.ttn.jobapp.Services.JobService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class JobServiceImpl implements JobService {

    @Autowired
    private JobRepository jr;

    @Override
    public Job save(Job job) {
        return jr.save(job);
    }

    @Override
    public List<Job> getJobs() {
        return jr.findAll();
    }

    @Override
    public void delete(Long id) {
        jr.deleteById(id);
    }

    @Override
    public Job getJobById(Long id) {
        return jr.findById(id).get();
    }

    @Override
    public List<JobDto> getFindingJobs(String keyword, Long cateId, String province) {

        List<Job> jobs = new ArrayList<>();

        if (cateId != null) {
            jobs.addAll(this.jr.findAllByCategory(cateId));
        }

        if (keyword != null) {
            jobs.addAll(this.jr.findAllByKeyword(keyword));
        }

        if (province != null) {
            jobs.addAll(this.jr.findAllByProvince(province));
        }

        if (keyword == null && cateId == null && province == null) {
            jobs.addAll(this.jr.findAll());
        }

        return jobs.stream()
                .distinct()
                .map(x -> {
                    JobDto jDto = new JobDto();
                    jDto.setId(x.getId());
                    jDto.setDetail(x.getDetail());
                    jDto.setAddress(x.getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(x.getCompany().getLogo());
                    jDto.setName(x.getName());
                    jDto.setCompanyId(x.getCompany().getId());
                    jDto.setCompanyName(x.getCompany().getName());
                    jDto.setSalary(x.getSalary());
                    jDto.setCategoryId(x.getCategory().getId());
                    jDto.setCreatedDate(LocalDate.now());
                    jDto.setEndDate(x.getEndDate());
                    jDto.setExperience(x.getExperience());
                    return jDto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public List<JobDto> getJobByCompany(Long companyId) {
        List<Job> jobs = this.jr.findAllByCompany(companyId);

        return jobs.stream()
                .map(x -> {
                    JobDto jDto = new JobDto();
                    jDto.setId(x.getId());
                    jDto.setDetail(x.getDetail());
                    jDto.setAddress(x.getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(x.getCompany().getLogo());
                    jDto.setName(x.getName());
                    jDto.setCompanyId(x.getCompany().getId());
                    jDto.setCompanyName(x.getCompany().getName());
                    jDto.setSalary(x.getSalary());
                    jDto.setCategoryId(x.getCategory().getId());
                    jDto.setCreatedDate(x.getCreatedDate());
                    jDto.setEndDate(x.getEndDate());
                    jDto.setExperience(x.getExperience());
                    return jDto;
                }).collect(Collectors.toList());

    }

    @Override
    public List<JobDto> getJobByCategory(Long categoryId) {
        List<Job> jobs = this.jr.findAllByCategory(categoryId);

        return jobs.stream()
                .map(x -> {
                    JobDto jDto = new JobDto();
                    jDto.setId(x.getId());
                    jDto.setDetail(x.getDetail());
                    jDto.setAddress(x.getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(x.getCompany().getLogo());
                    jDto.setName(x.getName());
                    jDto.setCompanyId(x.getCompany().getId());
                    jDto.setCompanyName(x.getCompany().getName());
                    jDto.setSalary(x.getSalary());
                    jDto.setCategoryId(x.getCategory().getId());
                    jDto.setCreatedDate(x.getCreatedDate());
                    jDto.setEndDate(x.getEndDate());
                    jDto.setExperience(x.getExperience());
                    return jDto;
                }).collect(Collectors.toList());

    }

    @Override
    public List<JobDto> getRecentJobs() {
        LocalDate sevenDaysAgo = LocalDate.now().minusDays(7);

        List<Job> jobs = jr.findRecentJobs(sevenDaysAgo);

        return jobs.stream()
                .map(x -> {
                    JobDto jDto = new JobDto();
                    jDto.setId(x.getId());
                    jDto.setDetail(x.getDetail());
                    jDto.setAddress(x.getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(x.getCompany().getLogo());
                    jDto.setName(x.getName());
                    jDto.setCompanyId(x.getCompany().getId());
                    jDto.setCompanyName(x.getCompany().getName());
                    jDto.setSalary(x.getSalary());
                    jDto.setCategoryId(x.getCategory().getId());
                    jDto.setCreatedDate(x.getCreatedDate());
                    jDto.setEndDate(x.getEndDate());
                    jDto.setExperience(x.getExperience());
                    return jDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<JobDto> getRelatedJobsByKeyword(Long jobId, String keyword) {
        Job j = this.jr.findById(jobId).get();
        List<Job> relatedJobs = new ArrayList<>();

        String[] keywords = keyword.toLowerCase().split("\\s+");
        for (String key : keywords) {
            List<Job> jobs = jr.findRelatedJobsByKeyword(key, jobId);
            relatedJobs.addAll(jobs);
        }

        relatedJobs.addAll(jr.findAllByCategory(j.getCategory().getId()));

        relatedJobs.addAll(jr.findAllByProvince(j.getCompany().getAddress().getProvince()));

        return relatedJobs.stream()
                .distinct()
                .map(x -> {
                    JobDto jDto = new JobDto();
                    jDto.setId(x.getId());
                    jDto.setDetail(x.getDetail());
                    jDto.setAddress(x.getCompany().getAddress().getProvince());
                    jDto.setCompanyLogo(x.getCompany().getLogo());
                    jDto.setName(x.getName());
                    jDto.setCompanyId(x.getCompany().getId());
                    jDto.setCompanyName(x.getCompany().getName());
                    jDto.setSalary(x.getSalary());
                    jDto.setCategoryId(x.getCategory().getId());
                    jDto.setCreatedDate(x.getCreatedDate());
                    jDto.setEndDate(x.getEndDate());
                    jDto.setExperience(x.getExperience());
                    return jDto;
                }).collect(Collectors.toList());
    }

    @Override
    public List<JobDto> findCompanyJobsByKeyword(String keyword, Long companyId) {
        List<Job> jobs = this.jr.findCompanyJobsByKeyword(keyword, companyId);
        
        List<JobDto> jDto = jobs.stream()
                .distinct()
                .map(x -> {
                    JobDto jobDto = new JobDto();
                    jobDto.setId(x.getId());
                    jobDto.setDetail(x.getDetail());
                    jobDto.setAddress(x.getCompany().getAddress().getProvince());
                    jobDto.setCompanyLogo(x.getCompany().getLogo());
                    jobDto.setName(x.getName());
                    jobDto.setCompanyId(x.getCompany().getId());
                    jobDto.setCompanyName(x.getCompany().getName());
                    jobDto.setSalary(x.getSalary());
                    jobDto.setCategoryId(x.getCategory().getId());
                    jobDto.setCreatedDate(x.getCreatedDate());
                    jobDto.setEndDate(x.getEndDate());
                    jobDto.setExperience(x.getExperience());
                    return jobDto;
                }).collect(Collectors.toList());
        
        return jDto;
    }

}
