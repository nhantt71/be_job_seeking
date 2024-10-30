
package com.ttn.jobapp.Dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RecruiterDto {

    @NotEmpty(message = "City is required")
    private String city;

    @NotEmpty(message = "Province is required")
    private String province;

    @NotEmpty(message = "Gender is required")
    private String gender;

    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    private Long companyId;
    
    private String fullname;
    
    private String phoneNumber;
    
    private Long id;
    
    private String email;
    
    private String avatar;
}
