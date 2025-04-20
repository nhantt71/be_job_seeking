package com.ttn.jobapp.Dto;

import java.time.LocalDateTime;
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

    private LocalDateTime updatedDate;

    private Long candidateId;
    
    private boolean mainCV;
    
    private MultipartFile imageFile;

   
}
