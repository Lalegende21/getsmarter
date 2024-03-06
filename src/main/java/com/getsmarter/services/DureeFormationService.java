package com.getsmarter.services;

import com.getsmarter.entities.DureeFormation;
import com.getsmarter.repositories.DureeFormationRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class DureeFormationService {

    private final DureeFormationRepo dureeFormationRepo;


    //Methode pour enregistrer une duree de formation
    public void saveDuree(DureeFormation dureeFormation) {

        Optional<DureeFormation> optionalDureeFormation = this.dureeFormationRepo.findByPeriode(dureeFormation.getPeriode());

        if (optionalDureeFormation.isPresent()) {
            throw new RuntimeException("Cette duree de formation existe deja !");
        }

        dureeFormation.setCreated_at(LocalDateTime.now());
        this.dureeFormationRepo.save(dureeFormation);
    }



    //Methode pour recuperer toutes es durees de formation
    public List<DureeFormation> getAllDuree() {
        return this.dureeFormationRepo.findAll();
    }



    //Methode pour recuperer une duree par son id
    public DureeFormation getDureeById(Long id) {
        Optional<DureeFormation> optionalDureeFormation = this.dureeFormationRepo.findById(id);
        return optionalDureeFormation.orElseThrow( () -> new RuntimeException("Duree with id: " +id+ " not found !"));
    }



    //Methode pour faire la mise a jour d'une duree
    public void updateDuree(Long id, DureeFormation dureeFormation) {
        DureeFormation updateDuree = this.getDureeById(id);
        if (updateDuree.getId().equals(dureeFormation.getId())) {
            updateDuree.setPeriode(dureeFormation.getPeriode());

            this.dureeFormationRepo.save(updateDuree);
        }else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer toutes les durees
    public void deleteAllDuree() {
        this.dureeFormationRepo.deleteAll();
    }



    //Methode pour supprimer une duree par id
    public void deleteById(Long id) {
        this.dureeFormationRepo.deleteById(id);
    }
}
