package com.resumebuilder.dto;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ExperienceDTO {
    private String id, company, position, location, description;
    private LocalDate startDate, endDate;
    private boolean current;
    private Integer displayOrder;
}
