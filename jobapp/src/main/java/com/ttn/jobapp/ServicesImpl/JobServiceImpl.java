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
    public Job getJobById(Long id){
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
 

}
