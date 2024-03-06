package com.getsmarter.services;

import com.getsmarter.entities.TypeFormation;
import com.getsmarter.repositories.TypeFormationRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TypeFormationService {

    private final TypeFormationRepo typeFormationRepo;


    //Methode pour enregister un type de formation
    public void saveTypeFormation(TypeFormation typeFormation) {

        //On verifie s'il n'existe pas deja un type de paiement avec le meme horaire
        Optional<TypeFormation> optionalTypeFormation = this.typeFormationRepo.findByHoraire(typeFormation.getHoraire());
        if (optionalTypeFormation.isPresent()) {
            throw new RuntimeException("Cette horaire existe deja !");
        }

        typeFormation.setCreated_at(LocalDateTime.now());
        this.typeFormationRepo.save(typeFormation);
    }



    //Methode pour recuperer tous les types de formations
    public List<TypeFormation> getAllTypeFormation() {
        return this.typeFormationRepo.findAll();
    }



    //Methode pour recuper un type de formation par son id
    public TypeFormation getTypeFormationById(Long id) {
        Optional<TypeFormation> optionalTypeFormation = this.typeFormationRepo.findById(id);
        return optionalTypeFormation.orElseThrow(() -> new RuntimeException("Type de formation with id: "+id+ " not found !"));
    }



    //Methode pour faire a mise a jour d'un type de formation
    public void updateTypeFormation(Long id, TypeFormation typeFormation) {
        TypeFormation updateTypeFormation = this.getTypeFormationById(id);

        if (updateTypeFormation.getId().equals(typeFormation.getId())) {
            updateTypeFormation.setHoraire(typeFormation.getHoraire());
            updateTypeFormation.setFormation(typeFormation.getFormation());
            this.typeFormationRepo.save(updateTypeFormation);
        } else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer tous les types de formtions
    public void deleteAllType() {
        this.typeFormationRepo.deleteAll();
    }



    //Methode pour supprimer un type par son id
    public void deleteTypeById(Long id) {
        this.typeFormationRepo.deleteById(id);
    }
}
