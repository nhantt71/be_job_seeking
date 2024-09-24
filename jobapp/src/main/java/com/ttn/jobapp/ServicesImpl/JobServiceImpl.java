/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.Job;
import com.ttn.jobapp.Repositories.JobRepository;
import com.ttn.jobapp.Services.JobService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */

@Service
public class JobServiceImpl implements JobService{
    
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
    
}
