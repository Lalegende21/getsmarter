package com.getsmarter.services;

import com.getsmarter.entities.StartCourse;
import com.getsmarter.repositories.StartCourseRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class StartCourseService {

    private final StartCourseRepo startCourseRepo;


    public List<StartCourse> getAllStartCourse() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.startCourseRepo.findAll(sort);
    }

    public StartCourse getStartCourse(Long id) {
        Optional<StartCourse> optionalStartCourse = this.startCourseRepo.findById(id);
        return optionalStartCourse.orElseThrow(() -> new EntityNotFoundException("Aucune matiere demarree trouvee!"));
    }

    public void deleteAllStartCourse() {
        this.startCourseRepo.deleteAll();
    }

    public void deleteStartCourseById(Long id) {
        this.startCourseRepo.deleteById(id);
    }
}
