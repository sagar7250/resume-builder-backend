package com.resumebuilder.service;

import com.resumebuilder.dto.*;
import com.resumebuilder.entity.User;
import com.resumebuilder.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + email));
    }

    public UserDTO getUserProfile(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return mapToDTO(user);
    }

    @Transactional
    public UserDTO updateProfile(String userId, UserDTO dto) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (dto.getFirstName() != null) user.setFirstName(dto.getFirstName());
        if (dto.getLastName() != null) user.setLastName(dto.getLastName());
        if (dto.getPhone() != null) user.setPhone(dto.getPhone());
        if (dto.getLocation() != null) user.setLocation(dto.getLocation());
        if (dto.getJobTitle() != null) user.setJobTitle(dto.getJobTitle());
        return mapToDTO(userRepository.save(user));
    }

    public UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .profilePicture(user.getProfilePicture())
                .phone(user.getPhone())
                .location(user.getLocation())
                .jobTitle(user.getJobTitle())
                .role(user.getRole().name())
                .resumeCount(user.getResumes() != null ? user.getResumes().size() : 0)
                .build();
    }
}
