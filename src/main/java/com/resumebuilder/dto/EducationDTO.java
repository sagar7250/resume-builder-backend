package com.resumebuilder.dto;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class EducationDTO {
    private String id, institution, degree, fieldOfStudy, location, grade, description;
    private LocalDate startDate, endDate;
    private boolean current;
    private Integer displayOrder;
}
