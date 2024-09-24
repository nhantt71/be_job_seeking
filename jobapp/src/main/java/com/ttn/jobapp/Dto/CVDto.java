package com.ttn.jobapp.Dto;

import jakarta.validation.constraints.NotEmpty;
import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Win11
 */
@Getter
@Setter
public class CVDto {

    @NotEmpty(message = "File CV is required")
    private String fileCV;

    @NotEmpty(message = "Name is required")
    private String name;

    private LocalDate updateDate;

    private Long employeeId; // Assuming you need to link to Employee
}
