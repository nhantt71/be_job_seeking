package com.ttn.jobapp.Dto;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Win11
 */
@Getter
@Setter
public class CVDto {

    private String fileCV;

    private String name;

    private LocalDate updateDate;

    private Long candidateId;
    
    private boolean mainCV;
    
    private MultipartFile imageFile;

}
