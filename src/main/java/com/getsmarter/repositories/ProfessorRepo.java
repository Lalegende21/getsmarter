package com.getsmarter.repositories;

import com.getsmarter.entities.Professor;
import com.getsmarter.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProfessorRepo extends JpaRepository<Professor, Long> {
    @Query("SELECT p FROM Professor p WHERE p.created_at >= :startDate ORDER BY p.created_at DESC")
    List<Professor> findRecentlyAddedProfessors(LocalDateTime startDate);

    Optional<Professor> findByPhoneNumber(String phoneNumber);

    Optional<Professor> findByEmail(String email);
}
