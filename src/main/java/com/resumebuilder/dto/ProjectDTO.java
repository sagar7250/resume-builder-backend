package com.resumebuilder.dto;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class ProjectDTO {
    private String id, name, description, technologies, liveUrl, githubUrl;
    private LocalDate startDate, endDate;
}
