package com.getsmarter.repositories;

import com.getsmarter.entities.Formation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface FormationRepo extends JpaRepository<Formation, Long> {
    Optional<Formation> findByName(String name);
}
