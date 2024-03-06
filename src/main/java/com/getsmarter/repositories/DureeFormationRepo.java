package com.getsmarter.repositories;


import com.getsmarter.entities.DureeFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DureeFormationRepo extends JpaRepository<com.getsmarter.entities.DureeFormation, Long> {
    Optional<DureeFormation> findByPeriode(String periode);
}
