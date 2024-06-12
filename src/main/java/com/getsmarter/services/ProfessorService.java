package com.getsmarter.services;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Image;
import com.getsmarter.entities.Professor;
import com.getsmarter.entities.Student;
import com.getsmarter.repositories.ImageRepo;
import com.getsmarter.repositories.ProfessorRepo;
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

@Service
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
public class ProfessorService {

    private final ProfessorRepo professorRepo;

    private ImageService imageService;

    private ImageRepo imageRepo;


    //Methode pour enregistrer un professeur
    public void saveProfessor(Professor professor) {
        Optional<Professor> optionalProfessor = this.professorRepo.findByEmail(professor.getEmail());
        if(optionalProfessor.isPresent()) {
            throw new RuntimeException("Un professeur avec cet email existe deja !");
        }

        Optional<Professor> optionalProfessor1 = this.professorRepo.findByPhoneNumber(professor.getPhoneNumber());
        if (optionalProfessor1.isPresent()) {
            throw new RuntimeException("Un professeur avec ce numero de telephone existe deja !");
        }

        professor.setCreated_at(LocalDateTime.now());
        this.professorRepo.save(professor);
    }



    @Transactional
    public Professor saveImageCenter(Long id, MultipartFile imageFile) throws IOException {
        // Vérifier la taille du fichier image
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Le poids de l'image ne doit pas depasser 5MB.");
        }

        // Définir l'URL de l'image sur l'entité Center
        Optional<Professor> optionalProfessor = this.professorRepo.findById(id);
        if (optionalProfessor.isEmpty()) {
            throw new EntityNotFoundException("Aucun professeur avec cet identifiant trouve !");
        }

        // Vérifier si une image existe déjà pour ce center
        Image existingImage = this.imageRepo.findByProfessor(optionalProfessor.get());
        if (existingImage != null) {
            // Supprimer l'image existante
            this.imageRepo.delete(existingImage);
        }

        // Enregistrer la nouvelle image
        Image newImage = this.imageService.uploadImageToFolder(imageFile);
        newImage.setProfessor(optionalProfessor.get());
        this.imageRepo.save(newImage);

        // Enregistrer le center avec l'URL de la nouvelle image
        optionalProfessor.get().setImage(newImage.getFilePath());
        return this.professorRepo.save(optionalProfessor.get());
    }




    //Methode pour recuperer tous les professeurs
    public List<Professor> getAllProfessor() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.professorRepo.findAll(sort);
    }



    //Methode pour recuperer les etudiants ajoutes recemment (1 derniers jours)
    public List<Professor> getRecentlyAddedProfessors() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 1 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.professorRepo.findRecentlyAddedProfessors(startDate);
    }



    //Methode pour recuperer un professeur par son id
    public Professor getProfessorById(Long id) {
        Optional<Professor> optionalProfessor = this.professorRepo.findById(id);
        return optionalProfessor.orElseThrow(() -> new EntityNotFoundException("Aucun professeur trouve avec cet identifiant !"));
    }


    //Methode pour modifier un professor
    public void updateProfessor(Long id, Professor professor) {
        Professor updateProfessor = this.getProfessorById(id);

        if (updateProfessor.getId().equals(professor.getId())) {
            updateProfessor.setFullName(professor.getFullName());
            updateProfessor.setEmail(professor.getEmail());
            updateProfessor.setPhoneNumber(professor.getPhoneNumber());
            updateProfessor.setCni(professor.getCni());
            updateProfessor.setCourse(professor.getCourse());
            this.professorRepo.save(updateProfessor);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id du professeur a modifie !");
        }
    }



    //Methode pour supprimer tous les professors
    public void deleteAllProfessor() {
        this.professorRepo.deleteAll();
    }


    //Methode pour supprimer un professor par son id
    public void deleteProfessorById(Long id) {
        this.professorRepo.deleteById(id);
    }
}
