package com.getsmarter.repositories;

import com.getsmarter.entities.CodeFormation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CodeFormationRepo extends JpaRepository<CodeFormation, Long> {
    Optional<CodeFormation> findByCode(String code);
}
