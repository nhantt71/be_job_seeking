
package com.ttn.jobapp.Pojo;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;

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
    
    private String name;
    
    private String experience;
    
    @Column(name = "detail", columnDefinition = "TEXT")
    private String detail;
    
    @Column(name = "created_date")
    private LocalDate createdDate;
    
    @Column(name = "end_date")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime endDate;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean enable = false;
    
    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;
    
    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;
    
    @OneToMany(mappedBy = "job", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<JobCandidate> jobCandidate;
}