package com.getsmarter.repositories;

import com.getsmarter.entities.Image;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepo extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String fileName);
}
