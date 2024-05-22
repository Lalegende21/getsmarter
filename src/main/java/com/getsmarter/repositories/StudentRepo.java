package com.getsmarter.repositories;

import com.getsmarter.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface StudentRepo extends JpaRepository<Student, Long> {
    Optional<Student> findByEmail(String email);

    Optional<Student> findByPhonenumber(String phonenumber);

    Optional<Student> findByMatricule(String matricule);

    Optional<Student> findByFirstnameAndLastname(String firstname, String lastname);

    @Query("SELECT s FROM Student s WHERE s.created_at >= :startDate ORDER BY s.created_at DESC")
    List<Student> findRecentlyAddedStudents(LocalDateTime startDate);
}
