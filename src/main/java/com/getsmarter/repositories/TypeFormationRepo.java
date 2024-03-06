package com.getsmarter.repositories;

import com.getsmarter.entities.TypeFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TypeFormationRepo extends JpaRepository<TypeFormation, Long> {
    Optional<TypeFormation> findByHoraire(String horaire);
}
