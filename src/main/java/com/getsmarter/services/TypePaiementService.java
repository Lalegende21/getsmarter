package com.getsmarter.services;

import com.getsmarter.entities.TypePaiement;
import com.getsmarter.repositories.TypePaiementRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class TypePaiementService {

    private final TypePaiementRepo typePaiementRepo;


    //Methode pour enregistrer un type de paiement
    public void saveTypePaiement(TypePaiement typePaiement) {

        Optional<TypePaiement> optionalTypePaiement = this.typePaiementRepo.findByDescription(typePaiement.getDescription());
        if (optionalTypePaiement.isPresent()) {
            throw new RuntimeException("Cette description existe deja !");
        }

        typePaiement.setDescription(typePaiement.getDescription().toUpperCase());
        typePaiement.setCreated_at(LocalDateTime.now());
        this.typePaiementRepo.save(typePaiement);
    }


    //Methode pour recuperer tous les types de paiement
    public List<TypePaiement> getAllTypePaiement() {
        return this.typePaiementRepo.findAll();
    }



    //Methode pour recuperer un type de paiement par son id
    public TypePaiement getTypePaiementById(Long id) {
        Optional<TypePaiement> optionalTypePaiement = this.typePaiementRepo.findById(id);
        return optionalTypePaiement.orElseThrow(() -> new RuntimeException("Type de paiement with id: "+id+" not found !"));
    }


    //Methode pour update un type de paiement
    public void updateTypePaiement(Long id, TypePaiement typePaiement) {
        TypePaiement updateTypePaiement = this.getTypePaiementById(id);

        if (updateTypePaiement.getId().equals(typePaiement.getId())) {
            updateTypePaiement.setDescription(typePaiement.getDescription());
            this.typePaiementRepo.save(updateTypePaiement);
        }
    }


    //Methode pour supprimer tous les types de paiement
    public void deleteAllTypePaiement() {
        this.typePaiementRepo.deleteAll();
    }


    //Methode pour supprimer un type de paiement par son id
    public void deleteTypePaiementById(Long id) {
        this.typePaiementRepo.deleteById(id);
    }
}
