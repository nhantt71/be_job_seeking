package com.ttn.jobapp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;
import lombok.Data;

@Entity
@Data
@Table(name = "candidate")
public class Candidate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullname;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean available = false;
    
    @OneToOne
    @JoinColumn(name = "account_id", unique = true, nullable = true)
    private Account account;
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CV> cvs;
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<JobCandidate> jobCandidate;
    
    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CompanyCandidate> companyCandidate;
    
}
