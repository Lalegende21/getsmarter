package com.getsmarter.services;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Formation;
import com.getsmarter.entities.Image;
import com.getsmarter.repositories.FormationRepo;
import com.getsmarter.repositories.ImageRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class FormationService {

    private final FormationRepo formationRepo;

    private final ImageService imageService;

    private final ImageRepo imageRepo;


    //Methode pour enregistrer une speciaite
    public Formation saveFormation(Formation formation) {

        Optional<Formation> optionalSpecialite = this.formationRepo.findBySpecialite(formation.getSpecialite());

        if (optionalSpecialite.isPresent()) {

            throw new RuntimeException("Ce nom de formation existe deja !");
        }

        formation.setCreated_at(LocalDateTime.now());
        return this.formationRepo.save(formation);
    }



    @Transactional
    public Formation saveImageCenter(Long id, MultipartFile imageFile) throws IOException {
        // Vérifier la taille du fichier image
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Le poids de l'image ne doit pas depasser 5MB.");
        }

        // Définir l'URL de l'image sur l'entité Center
        Optional<Formation> optionalFormation = this.formationRepo.findById(id);
        if (optionalFormation.isEmpty()) {
            throw new EntityNotFoundException("Aucun campus avec cet identifiant trouve !");
        }

        // Vérifier si une image existe déjà pour ce center
        Image existingImage = this.imageRepo.findByFormation(optionalFormation.get());
        if (existingImage != null) {
            // Supprimer l'image existante
            this.imageRepo.delete(existingImage);
        }

        // Enregistrer la nouvelle image
        Image newImage = this.imageService.uploadImageToFolder(imageFile);
        newImage.setFormation(optionalFormation.get());
        this.imageRepo.save(newImage);

        // Enregistrer le center avec l'URL de la nouvelle image
        optionalFormation.get().setImage(newImage.getFilePath());
        return this.formationRepo.save(optionalFormation.get());
    }



    //Methode pour recuperer toutes les specialites
    public List<Formation> getAllFormations() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.formationRepo.findAll(sort);
    }



    //Methode pour recuperer les formations ajoutes recemment (1 derniers jours)
    public List<Formation> getRecentlyAddedFormations() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 7 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.formationRepo.findRecentlyAddedFormations(startDate);
    }



    //Methode pour recuperer une fiiere par id
    public Formation getFormationById(Long id) {
        Optional<Formation> optionalSpecialite = this.formationRepo.findById(id);
        return optionalSpecialite.orElseThrow(() -> new RuntimeException("Aucune formation avec cet identifiant: " +id+ " trouve !"));
    }



    //Methode pour faire la mise a jour d'une speciaite
    public void updateFormation(Long id, Formation formation) {
        Formation updateFormation = this.getFormationById(id);

        Optional<Formation> optionalFormation = this.formationRepo.findById(formation.getId());

        if (updateFormation.getId().equals(formation.getId())) {
            updateFormation.setSpecialite(formation.getSpecialite());
            updateFormation.setSpecificiteFormation(formation.getSpecificiteFormation());
            updateFormation.setPrice(formation.getPrice());
            updateFormation.setPeriode(formation.getPeriode());
            this.formationRepo.save(updateFormation);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id de la formation a modifie !");
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
