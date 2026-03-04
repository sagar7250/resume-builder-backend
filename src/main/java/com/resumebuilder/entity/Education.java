package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "educations")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Education {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String institution;
    @Column(nullable = false) private String degree;
    private String fieldOfStudy, location, grade;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    @Column(columnDefinition = "TEXT") private String description;
    private Integer displayOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") private Resume resume;
}
