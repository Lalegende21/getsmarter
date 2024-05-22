package com.getsmarter.repositories;

import com.getsmarter.entities.Course;
import com.getsmarter.entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface CourseRepo extends JpaRepository<Course, Long> {
    @Query("SELECT c FROM Course c WHERE c.created_at >= :startDate ORDER BY c.created_at DESC")
    List<Course> findRecentlyAddedCourse(LocalDateTime startDate);

    Optional<Course> findByName(String name);
}
