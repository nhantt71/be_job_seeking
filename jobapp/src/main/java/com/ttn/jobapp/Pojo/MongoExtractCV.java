/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;
import jakarta.persistence.Id;
import lombok.Data;
import org.springframework.data.mongodb.core.mapping.Document;
/**
 *
 * @author Win11
 */
@Document(collection = "extract-cv")
@Data
public class MongoExtractCV {
    @Id
    private String id;
    
    private String gender;
    
    private String experience;
    
    private String skill;
    
    private String language;
    
    private String education;
    
    private String certification;
    
    private String goal;
    
    private Long candidateId;
}
