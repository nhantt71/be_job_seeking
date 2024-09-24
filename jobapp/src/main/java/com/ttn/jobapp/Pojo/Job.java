
package com.ttn.jobapp.Pojo;


import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@lombok.Getter
@lombok.Setter
@Table(name = "job")
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String salary;
    
    private String experience;
    private String detail;
    
    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "end_date")
    private LocalDate endDate;
    
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
}