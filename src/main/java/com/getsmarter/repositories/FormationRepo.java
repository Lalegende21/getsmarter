package com.getsmarter.repositories;

import com.getsmarter.entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FormationRepo extends JpaRepository<Formation, Long> {
    Optional<Formation> findBySpecialite(String specialite);

    @Query("SELECT f FROM Formation f WHERE f.created_at >= :startDate ORDER BY f.created_at DESC")
    List<Formation> findRecentlyAddedFormations(LocalDateTime startDate);
}
