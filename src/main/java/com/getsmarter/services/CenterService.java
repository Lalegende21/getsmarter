package com.getsmarter.services;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Image;
import com.getsmarter.entities.Student;
import com.getsmarter.repositories.CenterRepo;
import com.getsmarter.repositories.ImageRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CenterService {

    private final CenterRepo centerRepo;
    private final ImageService imageService;

    private final ImageRepo imageRepo;



    //Methode pour enregistrer un centre
    public void saveCenter(Center center) {

        Optional<Center> optionalCenter = this.centerRepo.findByName(center.getName());

        if (optionalCenter.isPresent()) {
            throw new RuntimeException("Ce nom de centre est deja utilise !");
        }

        center.setCreated_at(LocalDateTime.now());
        this.centerRepo.save(center);
    }





    @Transactional
    public Center saveImageCenter(Long id, MultipartFile imageFile) throws IOException {
        // Vérifier la taille du fichier image
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Le poids de l'image ne doit pas depasser 5MB.");
        }

        // Définir l'URL de l'image sur l'entité Center
        Optional<Center> optionalCenter = this.centerRepo.findById(id);
        if (optionalCenter.isEmpty()) {
            throw new EntityNotFoundException("Aucun campus avec cet identifiant trouve !");
        }

        // Vérifier si une image existe déjà pour ce center
        Image existingImage = this.imageRepo.findByCenter(optionalCenter.get());
        if (existingImage != null) {
            // Supprimer l'image existante
            this.imageRepo.delete(existingImage);
        }

        // Enregistrer la nouvelle image
        Image newImage = this.imageService.uploadImageToFolder(imageFile);
        newImage.setCenter(optionalCenter.get());
        this.imageRepo.save(newImage);

        // Enregistrer le center avec l'URL de la nouvelle image
        optionalCenter.get().setImage(newImage.getFilePath());
        return this.centerRepo.save(optionalCenter.get());
    }



    //Methode pour recuperer tous les centres
    public List<Center> getAllCenter() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.centerRepo.findAll(sort);
    }



    //Methode pour recuperer les etudiants ajoutes recemment (1 derniers jours)
    public List<Center> getRecentlyAddedCenters() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 7 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.centerRepo.findRecentlyAddedCenters(startDate);
    }



    //Methode pour recuper un centre par son id
    public Center getCenterById(Long id) {
        Optional<Center> optionalCenter = this.centerRepo.findById(id);
        return optionalCenter.orElseThrow(() -> new RuntimeException("Aucun centre avec l'identifiant: " +id+ " trouve !"));
    }


    //Methode pour recuperer un centre par son nom
    public Center getCenterByName(String name) {
        Optional<Center> optionalCenter = this.centerRepo.findByName(name);
        return optionalCenter.orElseThrow(() -> new RuntimeException("Aucun centre avec le nom: "+name+ " trouve !"));
    }



    //Methode pour faire la mise a jour des donnes du centre
    public void updateCenter(Long id, Center center) {
        Center updateCenter = this.getCenterById(id);

        if (updateCenter.getId().equals(center.getId())) {
            updateCenter.setName(center.getName());
            updateCenter.setLocalisation(center.getLocalisation());
            this.centerRepo.save(updateCenter);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id du centre a modifie !");
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
