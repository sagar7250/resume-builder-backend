package com.resumebuilder.repository;

import com.resumebuilder.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    @Query("SELECT COUNT(r) FROM Resume r WHERE r.user.id = :userId")
    int countResumesByUserId(String userId);
}