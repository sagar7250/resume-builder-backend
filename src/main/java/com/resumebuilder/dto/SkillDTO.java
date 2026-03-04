package com.resumebuilder.dto;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class SkillDTO {
    private String id, name, level, category;
}
