package com.resumebuilder.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import java.time.LocalDateTime;
import java.util.*;

@Entity @Table(name = "users")
@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class User implements UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.UUID)
    private String id;
    @Column(nullable = false, unique = true) private String email;
    @Column(nullable = false) private String password;
    @Column(nullable = false) private String firstName;
    @Column(nullable = false) private String lastName;
    private String profilePicture, phone, location, jobTitle;
    @Enumerated(EnumType.STRING) @Builder.Default private Role role = Role.USER;
    @Builder.Default private boolean enabled = true;
    @Column(updatable = false) @Builder.Default private LocalDateTime createdAt = LocalDateTime.now();
    @Builder.Default private LocalDateTime updatedAt = LocalDateTime.now();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default private List<Resume> resumes = new ArrayList<>();
    @PreUpdate public void preUpdate() { this.updatedAt = LocalDateTime.now(); }
    @Override public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }
    @Override public String getUsername() { return email; }
    @Override public boolean isAccountNonExpired() { return true; }
    @Override public boolean isAccountNonLocked() { return true; }
    @Override public boolean isCredentialsNonExpired() { return true; }
    public enum Role { USER, ADMIN, PREMIUM }
}
