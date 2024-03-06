package com.getsmarter.repositories;

import com.getsmarter.entities.Facture;
import com.getsmarter.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FactureRepo extends JpaRepository<Facture, Long> {
    Optional<Facture> findByStudent(Student student);
}
