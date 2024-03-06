package com.getsmarter.services;

import com.getsmarter.entities.Center;
import com.getsmarter.repositories.CenterRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CenterService {

    private final CenterRepo centerRepo;



    //Methode pour enregistrer un centre
    public void saveCenter(Center center) {

        Optional<Center> optionalCenter = this.centerRepo.findByName(center.getName());

        if (optionalCenter.isPresent()) {
            throw new RuntimeException("Ce nom de centre est deja utilise !");
        }

        center.setCreated_at(LocalDateTime.now());
        this.centerRepo.save(center);
    }



    //Methode pour recuperer tous les centres
    public List<Center> getAllCenter() {
        return this.centerRepo.findAll();
    }



    //Methode pour recuper un centre par son id
    public Center getCenterById(Long id) {
        Optional<Center> optionalCenter = this.centerRepo.findById(id);
        return optionalCenter.orElseThrow(() -> new RuntimeException("Center with id :" +id+ " not found !"));
    }



    //Methode pour faire la mise a jour des donnes du centre
    public void updateCenter(Long id, Center center) {
        Center updateCenter = this.getCenterById(id);

        if (updateCenter.getId().equals(center.getId())) {
            updateCenter.setName(center.getName());
            updateCenter.setLocalisation(center.getLocalisation());
            this.centerRepo.save(updateCenter);
        } else {
            throw new RuntimeException("Something went wrong !");
        }
    }



    //Methode pour supprimer tous les centres
    public void deleteAllCenter() {
        this.centerRepo.deleteAll();
    }



    //Methode pour supprimer un centre par son id
    public void deleteCenterById(Long id) {
        this.centerRepo.deleteById(id);
    }
}
