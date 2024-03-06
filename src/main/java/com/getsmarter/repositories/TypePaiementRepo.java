package com.getsmarter.repositories;

import com.getsmarter.entities.TypePaiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TypePaiementRepo extends JpaRepository<TypePaiement, Long> {
    Optional<TypePaiement> findByDescription(String description);
}
