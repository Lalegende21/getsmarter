package com.getsmarter.services;

import com.getsmarter.entities.TypeTutor;
import com.getsmarter.repositories.TypeTutorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TypeTutorService {

    private final TypeTutorRepo typeTutorRepo;


    //Methode pour enregistrer un type de tuteur
    public void saveTypteTutor(TypeTutor typeTutor) {

        Optional<TypeTutor> optionalTypeTutor = this.typeTutorRepo.findByType(typeTutor.getType());
        if (optionalTypeTutor.isPresent()) {
            throw new RuntimeException("Ce type de tuteur existe deja !");
        }

        typeTutor.setCreated_at(LocalDateTime.now());
        this.typeTutorRepo.save(typeTutor);
    }



    //Methode pour recuperer tous les types de tuteur
    public List<TypeTutor> getAllTypeTutor() {
        return this.typeTutorRepo.findAll();
    }



    //Methode pour recuperer un type de tuteur par son id
    public TypeTutor getTypeTutorById(Long id) {
        Optional<TypeTutor> optionalTypeTutor = this.typeTutorRepo.findById(id);
        return optionalTypeTutor.orElseThrow(() -> new RuntimeException("Type tutor with id: "+id+" not found !"));
    }



    //Methode pour update un type de tuteur
    public void updateTypeTutor(Long id, TypeTutor typeTutor) {
        TypeTutor updateTypeTutor = this.getTypeTutorById(id);

        if (updateTypeTutor.getId().equals(typeTutor.getId())) {
            updateTypeTutor.setType(typeTutor.getType());

            this.typeTutorRepo.save(updateTypeTutor);
        } else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer tous les types de tuteur
    public void deleteAllTypeTutor() {
        this.typeTutorRepo.deleteAll();
    }



    //Mehtode pour supprimer un type de tuteur par son id
    public void deleteTypeTutorById(Long id) {
        this.typeTutorRepo.deleteById(id);
    }
}
