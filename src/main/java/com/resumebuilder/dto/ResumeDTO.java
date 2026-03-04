package com.resumebuilder.dto;

import com.resumebuilder.entity.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Data @Builder @NoArgsConstructor @AllArgsConstructor
public class ResumeDTO {
    private String id;
    private String title;
    private String template;
    private boolean published;
    private Integer atsScore;

    private String fullName, email, phone, location, website, linkedin, github, summary;

    private List<ExperienceDTO> experiences;
    private List<EducationDTO> educations;
    private List<SkillDTO> skills;
    private List<ProjectDTO> projects;
    private List<CertificationDTO> certifications;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
