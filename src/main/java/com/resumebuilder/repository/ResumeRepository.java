package com.resumebuilder.repository;

import com.resumebuilder.entity.Resume;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ResumeRepository extends JpaRepository<Resume, String> {
    List<Resume> findByUserIdOrderByUpdatedAtDesc(String userId);
    Optional<Resume> findBySlug(String slug);
    Optional<Resume> findByIdAndUserId(String id, String userId);
    @Query("SELECT r FROM Resume r WHERE r.user.id = :userId AND r.published = true")
    List<Resume> findPublishedByUserId(@Param("userId") String userId);
    long countByUserId(String userId);
}