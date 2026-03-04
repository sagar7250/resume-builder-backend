package com.resumebuilder.dto;

import lombok.*;
import java.time.LocalDate;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class CertificationDTO {
    private String id, name, issuer, credentialId, credentialUrl;
    private LocalDate issueDate, expiryDate;
}
