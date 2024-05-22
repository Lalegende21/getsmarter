package com.getsmarter.repositories;

import com.getsmarter.entities.Student;
import com.getsmarter.entities.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface TutorRepo extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByEmail(String email);

    Optional<Tutor> findByPhonenumber(String phonenumber);

    Optional<Tutor> findByFullname(String fullname);

    @Query("SELECT t FROM Tutor t WHERE t.created_at >= :startDate ORDER BY t.created_at DESC")
    List<Tutor> findRecentlyAddedTutors(LocalDateTime startDate);
}
