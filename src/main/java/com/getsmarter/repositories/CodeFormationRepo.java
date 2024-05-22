package com.getsmarter.repositories;

import com.getsmarter.entities.SpecificiteFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CodeFormationRepo extends JpaRepository<SpecificiteFormation, Long> {
    Optional<SpecificiteFormation> findByCode(String code);
}
