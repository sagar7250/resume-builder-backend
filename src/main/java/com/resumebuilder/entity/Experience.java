package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "experiences")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Experience {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String company;
    @Column(nullable = false) private String position;
    private String location;
    private LocalDate startDate;
    private LocalDate endDate;
    private boolean current;
    @Column(columnDefinition = "TEXT") private String description;
    private Integer displayOrder;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") private Resume resume;
}
