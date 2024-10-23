/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Services;

import com.ttn.jobapp.Pojo.MongoExtractCV;

/**
 *
 * @author Win11
 */
public interface MongoExtractCVService {
    MongoExtractCV extractCVInformation(String extractedText, Long candidateId);
}
