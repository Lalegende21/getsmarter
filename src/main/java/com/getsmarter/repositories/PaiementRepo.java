package com.getsmarter.repositories;

import com.getsmarter.entities.Paiement;
import com.getsmarter.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public interface PaiementRepo extends JpaRepository<Paiement, Long> {
    @Query("SELECT p FROM Paiement p WHERE p.created_at >= :startDate ORDER BY p.created_at DESC")
    List<Paiement> findRecentlyAddedPaiements(LocalDateTime startDate);
}
