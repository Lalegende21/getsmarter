package com.getsmarter.services;

import com.getsmarter.entities.Admin;
import com.getsmarter.entities.Tutor;
import com.getsmarter.entities.TypeTutor;
import com.getsmarter.repositories.TutorRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TutorService {

    private final TutorRepo tutorRepo;


    //Methode pour enregistrer un tuteur
    public void saveTutor(Tutor tutor) {

        // On verifie si l'email contient le symbole @
        if (!tutor.getEmail().contains("@")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si l'email contient un .
        if (!tutor.getEmail().contains(".")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si un utilisateur avec l'email donnee existe deja
        Optional<Tutor> optionalTutor = this.tutorRepo.findByEmail(tutor.getEmail());
        if (optionalTutor.isPresent()){
            throw new RuntimeException("Votre email est déjà utilisée!");
        }

        //On verifie si un utilisateur avec le numero de telephone donnee existe deja
        Optional<Tutor> optionalTutorPhoneNumber = this.tutorRepo.findByPhonenumber(tutor.getPhonenumber());
        if (optionalTutorPhoneNumber.isPresent()) {
            throw new RuntimeException("Votre numero de telephone est déjà utilisée!");
        }

        tutor.setCreated_at(LocalDateTime.now());
        this.tutorRepo.save(tutor);
    }


    //Methode pour recuperer tous les tuteurs
    public List<Tutor> getAllTutor() {
        return this.tutorRepo.findAll();
    }


    //Methode pour recuperer un tuteur par id
    public Tutor getTutorById(Long id) {
        Optional<Tutor> optionalTutor = this.tutorRepo.findById(id);
        return optionalTutor.orElseThrow(() -> new RuntimeException("Tutor with id "+id+" not found !"));
    }


    //Methode pour faire la mise a jour des donnees d'un tuteur
    public void updateTutor(Long id, Tutor tutor) {
        Tutor updateTutor = this.getTutorById(id);

        if (updateTutor.getId().equals(tutor.getId())) {
            updateTutor.setFullname(tutor.getFullname());
            updateTutor.setEmail(tutor.getEmail());
            updateTutor.setPhonenumber(tutor.getPhonenumber());
            updateTutor.setType(tutor.getType());
            this.tutorRepo.save(updateTutor);
        } else {
            throw new RuntimeException("Something went wrong !");
        }
    }


    //Methode pour supprimer tous les tuteurs
    public void deleteAllTutor() {
        this.tutorRepo.deleteAll();
    }


    //Methode pour supprimer les tuteurs par id
    public void deleteTutorById(Long id) {
        this.tutorRepo.deleteById(id);
    }
}
