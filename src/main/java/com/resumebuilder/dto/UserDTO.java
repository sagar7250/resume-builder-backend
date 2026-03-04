package com.resumebuilder.dto;

import lombok.*;

@Data @Builder @AllArgsConstructor @NoArgsConstructor
public class UserDTO {
    private String id, email, firstName, lastName;
    private String profilePicture, phone, location, jobTitle, role;
    private int resumeCount;
}
