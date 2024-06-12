package com.getsmarter.repositories;

import com.getsmarter.entities.StartCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface StartCourseRepo extends JpaRepository<StartCourse, Long> {
}
