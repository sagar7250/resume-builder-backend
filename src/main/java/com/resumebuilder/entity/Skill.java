package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity @Table(name = "skills")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Skill {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String name;
    @Enumerated(EnumType.STRING) @Builder.Default private Level level = Level.INTERMEDIATE;
    private String category;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") private Resume resume;
    public enum Level { BEGINNER, INTERMEDIATE, ADVANCED, EXPERT }
}
