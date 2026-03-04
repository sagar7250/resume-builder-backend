package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "resumes")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Resume {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false) private String title;
    @Enumerated(EnumType.STRING) @Builder.Default private Template template = Template.MODERN;
    @Builder.Default private boolean published = false;
    private String slug;
    private Integer atsScore;

    // Personal Info
    private String fullName, email, phone, location, website, linkedin, github, summary;

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC") @Builder.Default
    private List<Experience> experiences = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @OrderBy("displayOrder ASC") @Builder.Default
    private List<Education> educations = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default private List<Skill> skills = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "resume", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default private List<Certification> certifications = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(updatable = false) @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate public void preUpdate() { this.updatedAt = LocalDateTime.now(); }

    public enum Template {
        FRESHER, MODERN, EXECUTIVE, CREATIVE, TECH, MINIMAL, ACADEMIC, CORPORATE
    }
}
