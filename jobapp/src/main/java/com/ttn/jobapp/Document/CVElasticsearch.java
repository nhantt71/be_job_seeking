/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Document;

/**
 *
 * @author Win11
 */

import java.util.Date;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Document(indexName = "cv_elasticsearch")
@Data
public class CVElasticsearch {
    
    @Id
    private String id;
    private String gender;
    private String education;
    private String skills;
    private String experience;
    private String language;
    private String goal;
    private Date dob;
    
}
