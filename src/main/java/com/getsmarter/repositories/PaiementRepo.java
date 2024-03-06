package com.getsmarter.repositories;

import com.getsmarter.entities.Paiement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PaiementRepo extends JpaRepository<Paiement, Long> {
}
