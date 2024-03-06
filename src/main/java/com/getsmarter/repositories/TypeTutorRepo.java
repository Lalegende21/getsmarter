package com.getsmarter.repositories;

import com.getsmarter.entities.TypeTutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TypeTutorRepo extends JpaRepository<TypeTutor, Long> {
    Optional<TypeTutor> findByType(String type);
}
