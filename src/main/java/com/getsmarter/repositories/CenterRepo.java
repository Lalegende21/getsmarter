package com.getsmarter.repositories;

import com.getsmarter.entities.Center;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CenterRepo extends JpaRepository<Center, Long> {
    Optional<Center> findByName(String name);
}
