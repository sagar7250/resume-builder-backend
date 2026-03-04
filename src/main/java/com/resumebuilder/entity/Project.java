package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "projects")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Project {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String name;
    @Column(columnDefinition = "TEXT") private String description;
    private String technologies, liveUrl, githubUrl;
    private LocalDate startDate, endDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") private Resume resume;
}
