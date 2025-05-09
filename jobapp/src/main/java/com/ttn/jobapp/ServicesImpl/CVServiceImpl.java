/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.CV;
import com.ttn.jobapp.Repositories.CVRepository;
import com.ttn.jobapp.Services.CVService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class CVServiceImpl implements CVService {

    @Autowired
    private CVRepository cvr;

    @Override
    public CV save(CV cv) {
        return cvr.save(cv);
    }

    @Override
    public List<CV> getCVs() {
        return cvr.findAll();
    }

    @Override
    public void delete(Long id) {
        cvr.deleteById(id);
    }

    @Override
    public List<CV> getCVsByCandidateId(Long id) {
        return this.cvr.getCVsByCandidateId(id);
    }

    @Override
    public CV getCVById(Long id) {
        return this.cvr.findById(id).get();
    }

    @Override
    public String getMainCVByCandidateId(Long id) {
        List<CV> cvs = this.cvr.getCVsByCandidateId(id);

        for (CV cv : cvs) {
            if (cv.getMainCV()) {
                return cv.getFileCV();
            }
        }

        return null;
    }


}
