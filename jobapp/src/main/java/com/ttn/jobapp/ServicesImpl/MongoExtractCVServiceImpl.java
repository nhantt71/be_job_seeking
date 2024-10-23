/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.ServicesImpl;

import com.ttn.jobapp.Pojo.MongoExtractCV;
import com.ttn.jobapp.Repositories.MongoExtractCVRepository;
import com.ttn.jobapp.Services.MongoExtractCVService;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Win11
 */
@Service
public class MongoExtractCVServiceImpl implements MongoExtractCVService {

    @Autowired
    private MongoExtractCVRepository cvRepository;

    @Override
    public MongoExtractCV extractCVInformation(String extractedText, Long candidateId) {
        MongoExtractCV mongoExtractCV = new MongoExtractCV();

        mongoExtractCV.setEducation(extractSection("Education", extractedText));
        mongoExtractCV.setGender(extractSection("Gender", extractedText));
        mongoExtractCV.setExperience(extractSection("Experience", extractedText));
        mongoExtractCV.setSkill(extractSection("Skill", extractedText));
        mongoExtractCV.setLanguage(extractSection("Language", extractedText));
        mongoExtractCV.setCertification(extractSection("Certification", extractedText));
        mongoExtractCV.setGoal(extractSection("Goal", extractedText));
        mongoExtractCV.setCandidateId(candidateId);


        return cvRepository.save(mongoExtractCV);
    }

    private String extractSection(String sectionName, String text) {
        String[] sectionKeywords = {"Skills", "Experience", "Education", "Certification", "Goal", "Gender"};

        String sectionPattern = sectionName + "\\s*[:|-]?\\s*(.*?)(?=(?:" + String.join("|", sectionKeywords) + ")\\s*:|$)";

        Pattern pattern = Pattern.compile(sectionPattern, Pattern.DOTALL | Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            return matcher.group(1).trim();
        }
        return "";
    }

}
