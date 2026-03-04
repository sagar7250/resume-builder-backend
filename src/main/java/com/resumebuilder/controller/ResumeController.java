package com.resumebuilder.controller;

import com.resumebuilder.dto.*;
import com.resumebuilder.entity.User;
import com.resumebuilder.service.ResumeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/resumes")
@RequiredArgsConstructor
@Tag(name = "Resumes", description = "Resume CRUD endpoints")
public class ResumeController {
    private final ResumeService resumeService;

    @GetMapping
    @Operation(summary = "Get all resumes for current user")
    public ResponseEntity<ApiResponse<List<ResumeDTO>>> getResumes(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(resumeService.getUserResumes(user.getId()), "Resumes fetched"));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get resume by ID")
    public ResponseEntity<ApiResponse<ResumeDTO>> getResume(
            @PathVariable String id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(resumeService.getResume(id, user.getId()), "Resume fetched"));
    }

    @PostMapping
    @Operation(summary = "Create new resume")
    public ResponseEntity<ApiResponse<ResumeDTO>> createResume(
            @RequestBody ResumeDTO dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(resumeService.createResume(dto, user.getId()), "Resume created"));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update resume")
    public ResponseEntity<ApiResponse<ResumeDTO>> updateResume(
            @PathVariable String id, @RequestBody ResumeDTO dto, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(resumeService.updateResume(id, dto, user.getId()), "Resume updated"));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete resume")
    public ResponseEntity<ApiResponse<Void>> deleteResume(
            @PathVariable String id, @AuthenticationPrincipal User user) {
        resumeService.deleteResume(id, user.getId());
        return ResponseEntity.ok(ApiResponse.success(null, "Resume deleted"));
    }

    @PostMapping("/{id}/duplicate")
    @Operation(summary = "Duplicate resume")
    public ResponseEntity<ApiResponse<ResumeDTO>> duplicateResume(
            @PathVariable String id, @AuthenticationPrincipal User user) {
        return ResponseEntity.ok(ApiResponse.success(resumeService.duplicateResume(id, user.getId()), "Resume duplicated"));
    }
}