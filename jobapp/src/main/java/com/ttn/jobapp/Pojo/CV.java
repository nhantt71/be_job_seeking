/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ttn.jobapp.Pojo;

/**
 *
 * @author Win11
 */
import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@lombok.Getter
@lombok.Setter
@Table(name = "cv")
public class CV {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "file_cv")
    private String fileCV;
    
    private String name;
    
    @Column(name = "updatedate")
    private LocalDate updateDate;

    @ManyToOne
    @JoinColumn(name = "account_id")
    private Account account;
}
