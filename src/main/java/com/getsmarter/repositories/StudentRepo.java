package com.getsmarter.repositories;

import com.getsmarter.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByPhonenumber(String phonenumber);

    Optional<Student> findByMatricule(String matricule);
}
