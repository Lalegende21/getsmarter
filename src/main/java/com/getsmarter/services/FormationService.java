package com.getsmarter.services;

import com.getsmarter.entities.Formation;
import com.getsmarter.repositories.FormationRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class FormationService {

    private final FormationRepo formationRepo;


    //Methode pour enregistrer une speciaite
    public Formation saveFormation(Formation formation) {

        Optional<Formation> optionalSpecialite = this.formationRepo.findByName(formation.getName());

        if (optionalSpecialite.isPresent()) {

            throw new RuntimeException("Ce nom de formation existe deja !");
        }

        formation.setCreated_at(LocalDateTime.now());
        return this.formationRepo.save(formation);
    }



    //Methode pour recuperer toutes les specialites
    public List<Formation> getAllFormations() {
        return this.formationRepo.findAll();
    }



    //Methode pour recuperer une fiiere par id
    public Formation getFormationById(Long id) {
        Optional<Formation> optionalSpecialite = this.formationRepo.findById(id);
        return optionalSpecialite.orElseThrow(() -> new RuntimeException("Formation with id: " +id+ " not found !"));
    }



    //Methode pour faire la mise a jour d'une speciaite
    public void updateFormation(Long id, Formation formation) {
        Formation updateFormation = this.getFormationById(id);

        if (updateFormation.getId().equals(formation.getId())) {
            updateFormation.setName(formation.getName());
            updateFormation.setCodeFormation(formation.getCodeFormation());
            updateFormation.setPrice(formation.getPrice());
            updateFormation.setDuree(formation.getDuree());

            this.formationRepo.save(updateFormation);
        } else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer toutes les speciaites
    public void deleteAllFrormation() {
        this.formationRepo.deleteAll();
    }



    //Methode pour supprimer ume speciaite par son id
    public void deleteFormationById(Long id) {
        this.formationRepo.deleteById(id);
    }
}
