/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

import java.time.LocalDate;
import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.annotation.Id;

/**
 *
 * @author Win11
 */
@Document(indexName = "cv_index")
@Data
public class CVElasticsearch {
    @Id
    private String id;
    private String name;
    private String phone;
    private String email;
    private String objective;
    private String education;
    private String experience;
    private LocalDate dOB;
    private String skills;
}
