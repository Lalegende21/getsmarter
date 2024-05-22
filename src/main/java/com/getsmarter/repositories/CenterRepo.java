package com.getsmarter.repositories;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CenterRepo extends JpaRepository<Center, Long> {
    Optional<Center> findByName(String name);

    @Query("SELECT c FROM Center c WHERE c.created_at >= :startDate ORDER BY c.created_at DESC")
    List<Center> findRecentlyAddedCenters(LocalDateTime startDate);
}
