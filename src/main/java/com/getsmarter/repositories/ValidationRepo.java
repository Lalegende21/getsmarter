package com.getsmarter.repositories;

import com.getsmarter.entities.Validation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.Optional;

public interface ValidationRepo extends JpaRepository<Validation, Long> {
    Optional<Validation> findByCode(String code);

    void deleteAllByExpireBefore(Instant now);
}
