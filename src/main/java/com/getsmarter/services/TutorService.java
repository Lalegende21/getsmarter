package com.getsmarter.services;

import com.getsmarter.entities.Student;
import com.getsmarter.entities.Tutor;
import com.getsmarter.repositories.StudentRepo;
import com.getsmarter.repositories.TutorRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class TutorService {

    private final TutorRepo tutorRepo;

    private final StudentRepo studentRepo;


    //Methode pour enregistrer un tuteur
    public void saveTutor(Tutor tutor) {

        //On verifie si le nom de l'utilisateur est vide
        if(tutor.getFullname().isEmpty()) {
            throw new RuntimeException("Votre nom ne peut etre vide!");
        }

        //On verifie si un utilisateur avec le nom donne existe deja
//        Optional<Tutor> optionalTutorFullname = this.tutorRepo.findByFullname(tutor.getFullname());
//        if(optionalTutorFullname.isPresent()) {
//            throw new RuntimeException("Votre nom existe deja!");
//        }

        //On verifie si un utilisateur avec le numero de telephone donne existe deja
        Optional<Tutor> optionalTutorPhoneNumber = this.tutorRepo.findByPhonenumber(tutor.getPhonenumber());
        if (optionalTutorPhoneNumber.isPresent()) {
            throw new RuntimeException("Votre numero de telephone est déjà utilisée!");
        }

        tutor.setCreated_at(LocalDateTime.now());
        this.tutorRepo.save(tutor);

    }


    //Methode pour recuperer tous les tuteurs
    public List<Tutor> getAllTutor() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.tutorRepo.findAll(sort);
    }


    //Methode pour recuperer les etudiants ajoutes recemment (1 derniers jours)
    public List<Tutor> getRecentlyAddedTutors() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 1 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.tutorRepo.findRecentlyAddedTutors(startDate);
    }


    //Methode pour recuperer un tuteur par id
    public Tutor getTutorById(Long id) {
        Optional<Tutor> optionalTutor = this.tutorRepo.findById(id);
        return optionalTutor.orElseThrow(() -> new EntityNotFoundException("Aucun tuteur trouve avec cet identifiant: "+id));
    }


    //Methode pour ajouter un etudiant au tuteur
    public void addStudentTotutor(Long id, Student student) {
        Tutor tutor = this.getTutorById(id);
        tutor.getStudents().add(student);
        tutorRepo.save(tutor);
    }

    //Methode pour supprimer un etudiant au tuteur
//    public void removeStudentToTutor(Long id, Student student) {
//        Tutor tutor = this.getTutorById(id);
//        tutor.getStudents().remove(student.getId());
//        tutorRepo.save(tutor);
//    }

    public void removeStudentToTutor(Long tutorId, Long studentId) {
        Tutor tutor = this.getTutorById(tutorId);
        Student studentToRemove = tutor.getStudents().stream()
                .filter(s -> s.getId().equals(studentId))
                .findFirst()
                .orElse(null);

        if (studentToRemove != null) {
            tutor.getStudents().remove(studentToRemove);
            tutorRepo.save(tutor);
        } else {
            // Gérer le cas où le Student n'est pas trouvé dans la liste
            throw new IllegalArgumentException("L'etudiant avec l'identifiant " + studentId + " pas trouve dans la liste des etudiant du tuteur.");
        }
    }


    //Methode pour faire la mise a jour des donnees d'un tuteur
    public void updateTutor(Long id, Tutor tutor) {
        Tutor updateTutor = this.getTutorById(id);

        if (updateTutor.getId().equals(tutor.getId())) {
            updateTutor.setFullname(tutor.getFullname());
            updateTutor.setEmail(tutor.getEmail());
            updateTutor.setPhonenumber(tutor.getPhonenumber());
            updateTutor.setTypeTutor(tutor.getTypeTutor());
            this.tutorRepo.save(updateTutor);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id du tuteur a modifie!");
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
