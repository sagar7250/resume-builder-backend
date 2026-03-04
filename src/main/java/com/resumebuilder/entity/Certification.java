package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity @Table(name = "certifications")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class Certification {
    @Id @GeneratedValue(strategy = GenerationType.UUID) private String id;
    @Column(nullable = false) private String name;
    private String issuer, credentialId, credentialUrl;
    private LocalDate issueDate, expiryDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resume_id") private Resume resume;
}
