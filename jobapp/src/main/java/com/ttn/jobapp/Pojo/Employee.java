package com.ttn.jobapp.Pojo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import java.util.List;

@Entity
@lombok.Getter
@lombok.Setter
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String fullname;
    
    @Column(name = "phone_number")
    private String phoneNumber;
    
    @OneToOne
    @JoinColumn(name = "account_id", unique = true)
    private Account account;
    
    @OneToMany(mappedBy = "employee", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<CV> cvs;
    

}
