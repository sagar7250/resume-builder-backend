package com.resumebuilder.service;

import com.resumebuilder.dto.*;
import com.resumebuilder.entity.*;
import com.resumebuilder.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.*;
import java.util.stream.Collectors;

@Service @RequiredArgsConstructor
public class ResumeService {

    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;

    public List<ResumeDTO> getUserResumes(String userId) {
        return resumeRepository.findByUserIdOrderByUpdatedAtDesc(userId)
                .stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    public ResumeDTO getResume(String resumeId, String userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        if (!resume.getUser().getId().equals(userId)) throw new RuntimeException("Access denied");
        return mapToDTO(resume);
    }

    @Transactional
    public ResumeDTO createResume(ResumeDTO dto, String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Resume resume = Resume.builder()
                .title(dto.getTitle() != null ? dto.getTitle() : "Untitled Resume")
                .template(parseTemplate(dto.getTemplate()))
                .user(user).build();
        resume = resumeRepository.save(resume);
        return mapToDTO(resume);
    }

    @Transactional
    public ResumeDTO updateResume(String resumeId, ResumeDTO dto, String userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        if (!resume.getUser().getId().equals(userId)) throw new RuntimeException("Access denied");

        if (dto.getTitle() != null) resume.setTitle(dto.getTitle());
        if (dto.getTemplate() != null) resume.setTemplate(parseTemplate(dto.getTemplate()));
        if (dto.getFullName() != null) resume.setFullName(dto.getFullName());
        if (dto.getEmail() != null) resume.setEmail(dto.getEmail());
        if (dto.getPhone() != null) resume.setPhone(dto.getPhone());
        if (dto.getLocation() != null) resume.setLocation(dto.getLocation());
        if (dto.getWebsite() != null) resume.setWebsite(dto.getWebsite());
        if (dto.getLinkedin() != null) resume.setLinkedin(dto.getLinkedin());
        if (dto.getGithub() != null) resume.setGithub(dto.getGithub());
        if (dto.getSummary() != null) resume.setSummary(dto.getSummary());

        if (dto.getExperiences() != null) {
            resume.getExperiences().clear();
            dto.getExperiences().forEach(e -> {
                Experience exp = new Experience();
                exp.setResume(resume);
                exp.setCompany(e.getCompany()); exp.setPosition(e.getPosition());
                exp.setLocation(e.getLocation()); exp.setStartDate(e.getStartDate());
                exp.setEndDate(e.getEndDate()); exp.setCurrent(e.isCurrent());
                exp.setDescription(e.getDescription()); exp.setDisplayOrder(e.getDisplayOrder());
                resume.getExperiences().add(exp);
            });
        }

        if (dto.getEducations() != null) {
            resume.getEducations().clear();
            dto.getEducations().forEach(e -> {
                Education edu = new Education();
                edu.setResume(resume);
                edu.setInstitution(e.getInstitution()); edu.setDegree(e.getDegree());
                edu.setFieldOfStudy(e.getFieldOfStudy()); edu.setGrade(e.getGrade());
                edu.setStartDate(e.getStartDate()); edu.setEndDate(e.getEndDate());
                edu.setCurrent(e.isCurrent()); edu.setDisplayOrder(e.getDisplayOrder());
                resume.getEducations().add(edu);
            });
        }

        if (dto.getSkills() != null) {
            resume.getSkills().clear();
            dto.getSkills().forEach(s -> {
                Skill skill = new Skill();
                skill.setResume(resume);
                skill.setName(s.getName());
                try { skill.setLevel(Skill.Level.valueOf(s.getLevel())); }
                catch (Exception ex) { skill.setLevel(Skill.Level.valueOf(s.getLevel())); }
                resume.getSkills().add(skill);
            });
        }

        if (dto.getProjects() != null) {
            resume.getProjects().clear();
            dto.getProjects().forEach(p -> {
                Project proj = new Project();
                proj.setResume(resume);
                proj.setName(p.getName()); proj.setDescription(p.getDescription());
                proj.setTechnologies(p.getTechnologies()); proj.setLiveUrl(p.getLiveUrl());
                proj.setGithubUrl(p.getGithubUrl());
                resume.getProjects().add(proj);
            });
        }

        if (dto.getCertifications() != null) {
            resume.getCertifications().clear();
            dto.getCertifications().forEach(c -> {
                Certification cert = new Certification();
                cert.setResume(resume);
                cert.setName(c.getName()); cert.setIssuer(c.getIssuer());
                cert.setIssueDate(c.getIssueDate()); cert.setExpiryDate(c.getExpiryDate());
                cert.setCredentialUrl(c.getCredentialUrl());
                resume.getCertifications().add(cert);
            });
        }

        resume.setAtsScore(calculateAtsScore(resume));
        return mapToDTO(resumeRepository.save(resume));
    }

    @Transactional
    public void deleteResume(String resumeId, String userId) {
        Resume resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));
        if (!resume.getUser().getId().equals(userId)) throw new RuntimeException("Access denied");
        resumeRepository.delete(resume);
    }

    @Transactional
    public ResumeDTO duplicateResume(String resumeId, String userId) {
        ResumeDTO original = getResume(resumeId, userId);
        original.setId(null);
        original.setTitle(original.getTitle() + " (Copy)");
        return createResume(original, userId);
    }

    private Resume.Template parseTemplate(String templateStr) {
        if (templateStr == null) return Resume.Template.MODERN;
        try { return Resume.Template.valueOf(templateStr.toUpperCase()); }
        catch (Exception e) { return Resume.Template.MODERN; }
    }

    private int calculateAtsScore(Resume r) {
        int score = 0;
        if (r.getFullName() != null && !r.getFullName().isEmpty()) score += 10;
        if (r.getEmail() != null && !r.getEmail().isEmpty()) score += 10;
        if (r.getPhone() != null && !r.getPhone().isEmpty()) score += 5;
        if (r.getSummary() != null && r.getSummary().length() > 50) score += 15;
        if (r.getExperiences() != null && !r.getExperiences().isEmpty()) score += 20;
        if (r.getEducations() != null && !r.getEducations().isEmpty()) score += 15;
        if (r.getSkills() != null && r.getSkills().size() >= 3) score += 15;
        if (r.getProjects() != null && !r.getProjects().isEmpty()) score += 5;
        if (r.getCertifications() != null && !r.getCertifications().isEmpty()) score += 5;
        return Math.min(score, 100);
    }

    public ResumeDTO mapToDTO(Resume r) {
        return ResumeDTO.builder()
                .id(r.getId()).title(r.getTitle())
                .template(r.getTemplate() != null ? r.getTemplate().name() : "MODERN")
                .published(r.isPublished()).atsScore(r.getAtsScore())
                .fullName(r.getFullName()).email(r.getEmail()).phone(r.getPhone())
                .location(r.getLocation()).website(r.getWebsite())
                .linkedin(r.getLinkedin()).github(r.getGithub()).summary(r.getSummary())
                .experiences(r.getExperiences() == null ? List.of() : r.getExperiences().stream().map(e ->
                    ExperienceDTO.builder().id(e.getId()).company(e.getCompany()).position(e.getPosition())
                        .location(e.getLocation()).startDate(e.getStartDate()).endDate(e.getEndDate())
                        .current(e.isCurrent()).description(e.getDescription()).displayOrder(e.getDisplayOrder()).build()
                ).collect(Collectors.toList()))
                .educations(r.getEducations() == null ? List.of() : r.getEducations().stream().map(e ->
                    EducationDTO.builder().id(e.getId()).institution(e.getInstitution()).degree(e.getDegree())
                        .fieldOfStudy(e.getFieldOfStudy()).grade(e.getGrade()).startDate(e.getStartDate())
                        .endDate(e.getEndDate()).current(e.isCurrent()).displayOrder(e.getDisplayOrder()).build()
                ).collect(Collectors.toList()))
                .skills(r.getSkills() == null ? List.of() : r.getSkills().stream().map(s ->
                    SkillDTO.builder().id(s.getId()).name(s.getName())
                        .level(s.getLevel() != null ? s.getLevel().name() : "INTERMEDIATE").build()
                ).collect(Collectors.toList()))
                .projects(r.getProjects() == null ? List.of() : r.getProjects().stream().map(p ->
                    ProjectDTO.builder().id(p.getId()).name(p.getName()).description(p.getDescription())
                        .technologies(p.getTechnologies()).liveUrl(p.getLiveUrl()).githubUrl(p.getGithubUrl()).build()
                ).collect(Collectors.toList()))
                .certifications(r.getCertifications() == null ? List.of() : r.getCertifications().stream().map(c ->
                    CertificationDTO.builder().id(c.getId()).name(c.getName()).issuer(c.getIssuer())
                        .issueDate(c.getIssueDate()).expiryDate(c.getExpiryDate()).credentialUrl(c.getCredentialUrl()).build()
                ).collect(Collectors.toList()))
                .createdAt(r.getCreatedAt()).updatedAt(r.getUpdatedAt()).build();
    }
}
