package com.getsmarter.repositories;

import com.getsmarter.entities.Tutor;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface TutorRepo extends JpaRepository<Tutor, Long> {
    Optional<Tutor> findByEmail(String email);

    Optional<Tutor> findByPhonenumber(String phonenumber);
}
