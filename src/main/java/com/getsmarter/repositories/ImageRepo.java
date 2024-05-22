package com.getsmarter.repositories;

import com.getsmarter.entities.*;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ImageRepo extends JpaRepository<Image, Long> {
    Optional<Image> findByName(String fileName);

    void deleteByName(String name);

    Image findByCenter(Center center);

    Image findByStudent(Student student);

    Image findByProfessor(Professor professor);

    Image findByFormation(Formation formation);

    Image findByCourse(Course course);
}
