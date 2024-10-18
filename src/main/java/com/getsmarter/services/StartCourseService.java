package com.getsmarter.services;

import com.getsmarter.entities.StartCourse;
import com.getsmarter.repositories.StartCourseRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StartCourseService {

    private final StartCourseRepo startCourseRepo;


    @Transactional(readOnly = true)
    @Cacheable(value = "startCourse")
    public List<StartCourse> getAllStartCourse() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.startCourseRepo.findAll(sort);
    }

    @Transactional(readOnly = true)
    @Cacheable(value = "startCourse", key = "#id")
    public StartCourse getStartCourse(Long id) {
        Optional<StartCourse> optionalStartCourse = this.startCourseRepo.findById(id);
        return optionalStartCourse.orElseThrow(() -> new EntityNotFoundException("Aucune matiere demarree trouvee!"));
    }

    @Transactional
    @CacheEvict(value = "startCourse", allEntries = true)
    public void deleteAllStartCourse() {
        this.startCourseRepo.deleteAll();
    }

    @Transactional
    @CacheEvict(value = "startCourse", key = "#id")
    public void deleteStartCourseById(Long id) {
        this.startCourseRepo.deleteById(id);
    }
}
