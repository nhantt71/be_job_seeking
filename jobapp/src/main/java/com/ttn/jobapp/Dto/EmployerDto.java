
package com.ttn.jobapp.Dto;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployerDto {

    @NotEmpty(message = "City is required")
    private String city;

    @NotEmpty(message = "Province is required")
    private String province;

    @NotEmpty(message = "Gender is required")
    private String gender;

    @NotNull(message = "Account ID cannot be null")
    private Long accountId;

    @NotNull(message = "Company ID cannot be null")
    private Long companyId;
    
    private String fullname;
    
    private String phoneNumber;
}
