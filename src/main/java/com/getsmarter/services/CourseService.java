package com.getsmarter.services;

import com.getsmarter.dto.StartCourseDto;
import com.getsmarter.entities.*;
import com.getsmarter.enums.Statut;
import com.getsmarter.mails.EmailScheduler;
import com.getsmarter.mails.EmailService;
import com.getsmarter.repositories.CourseRepo;
import com.getsmarter.repositories.StartCourseRepo;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class CourseService {

    private final CourseRepo courseRepo;
    private final EmailScheduler emailScheduler;
    private final EmailService emailService;
    private final StartCourseRepo startCourseRepo;
//    private final ImageService imageService;
//    private final ImageRepo imageRepo;


    //Methode pour enregistrer une cours
    public void saveCourse(Course course) {

        Optional<Course> courseOptional = this.courseRepo.findByName(course.getName());
        if(courseOptional.isPresent()) {
            throw new RuntimeException("Cette matiere existe deja !");
        }
        course.setCreated_at(LocalDateTime.now());
        this.courseRepo.save(course);
    }


    //Methode pour changer le statut d'une matiere
    public void changeStatut(Long id, StartCourseDto startCourseDto) {
        Optional<Course> optionalCourse = Optional.ofNullable(this.courseRepo.findById(id).orElseThrow(() -> new RuntimeException("Course not found")));
//        optionalCourse.get().setStatut(Statut.ACTIVER);


        // Obtenir la date actuelle
        LocalDate currentDate = LocalDate.now();

        //Je recupere le nom et la duree du cours(Ex: 1) et j'extrait le caractere que je convertir en number
        String dureeCourse = optionalCourse.get().getDureeCourse();
        String nameCourse = optionalCourse.get().getName().toUpperCase();
        System.out.println("DureeCourse: "+ dureeCourse);
        int duree = Integer.parseInt(dureeCourse);

        //Calcul de a date de fin en fonction de la duree cours
        LocalDate dateDeFinDuCours = currentDate.plus(duree, ChronoUnit.WEEKS);
        System.out.println(dateDeFinDuCours);

        String to = optionalCourse.get().getProfessor().getEmail();
        System.out.println("Professor email: "+ to);
        String subject = "Rappel sur le duree de cours! \uD83D\uDD52\n";
        String text;

        StartCourse startCourse = StartCourse.builder()
                .name(optionalCourse.get().getName())
                .professor(optionalCourse.get().getProfessor().getFullName())
                .duree(optionalCourse.get().getDureeCourse())
                .statut(Statut.ACTIVER)
                .startDate(currentDate.toString())
                .endDate(dateDeFinDuCours.toString())
                .session(startCourseDto.getSession())
                .horaire(startCourseDto.getHoraire())
                .created_at(LocalDateTime.now())
                .build();

        if(dateDeFinDuCours.isAfter(currentDate)) {
            this.startCourseRepo.save(startCourse);
            text = "Bonjour Très cher Professeur " + optionalCourse.get().getProfessor().getFullName() +
                    "\n\n" +
                    "Nous tenons à vous informer qu'aujourd'hui le "+ currentDate +"⏳ marque le début du cours de " + nameCourse +
                    " qui s'étendra sur "+ dureeCourse + " semaine(s) et dont la fin sera le "+ dateDeFinDuCours + "."+
                    "\n" +
                    "Merci de bien vouloir respecter ce délai! \uD83D\uDCDA\uD83D\uDCAA\n" +
                    "\n\n" +
                    "✅ Date de debut du cours: " + currentDate + "\n" +
                    "✅ Date de fin du cours: " + dateDeFinDuCours + "\n" +
                    "✅ Duree allouee : " + dureeCourse + " semaines \n\n" +
                    "Cordialement, GetSmarter! \uD83C\uDF1F";
            emailScheduler.setTo(to);
            emailScheduler.setSubject(subject);
            emailScheduler.setText(text);
            emailScheduler.sendWeeklyEmail();
            System.out.println("Email sent successfully !");
            this.courseRepo.save(optionalCourse.get());
        } else {
            this.startCourseRepo.save(startCourse);
            text = "Cher Professeur ," + optionalCourse.get().getProfessor().getFullName() + " \uD83D\uDD52\n" +
                    "\n" +
                    "C'est la fin du temps aloue pour la matiere: " + nameCourse + " ⏳\n" +
                    "\n" +
                    "Nous vous remercions pour votre devouement a transmettre vos connaissances aux etudiant! \uD83D\uDCDA\uD83D\uDCAA\n" +
                    "\n" +
                    "Bonne journée! \uD83C\uDF1F";
            this.emailService.sendEmail(to, subject, text);
            System.out.println("Email sent successfully !");
            this.courseRepo.save(optionalCourse.get());
        }
    }



    //Methode pour recuperer tous les cours
    public List<Course> getAllCourse() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.courseRepo.findAll(sort);
    }



    //Methode pour recuperer les matieres ajoutes recemment (1 derniers jours)
    public List<Course> getRecentlyAddedCourses() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 1 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.courseRepo.findRecentlyAddedCourse(startDate);
    }




    //Methode pour recuperer un cours par son id
    public Course getCourseById(Long id) {
        Optional<Course> optionalCourse = this.courseRepo.findById(id);
        return optionalCourse.orElseThrow(() -> new EntityNotFoundException("Aucune matiere trouve avec cet identifiant !"));
    }


    //Methode pour modifier un cours
    public void updateCourse(Long id, Course course) {
        Course updateCourse = this.getCourseById(id);

//        if (updateCourse.getStatut() != null) {
//            if (updateCourse.getStatut().equals(Statut.ACTIVER)) {
//                throw new RuntimeException("Cette matiere a deja demarree et ne peut plus etre modifiee !");
//            } else if (updateCourse.getStatut().equals(Statut.TERMINER)) {
//                throw new RuntimeException("Cette matiere est deja terminee et ne peut plus etre modifiee !");
//            } else {
                if (updateCourse.getId().equals(course.getId())) {
                    updateCourse.setName(course.getName());
                    updateCourse.setProfessor(course.getProfessor());
                    updateCourse.setDureeCourse(course.getDureeCourse());
//                    updateCourse.setStatut(course.getStatut());
                    this.courseRepo.save(updateCourse);
                } else {
                    throw new RuntimeException("Incoherence entre l'id fourni et l'id de la matiere a modifie !");
                }
//            }
//        } else {
//            throw new RuntimeException("Le statut du cours est inconnu.");
//        }
    }



    //Methode pour supprimer tous les cours
    public void deleteAllCourse() {
        this.courseRepo.deleteAll();
    }


    //Methode pour supprimer un cours son id
    public void deleteCourseById(Long id) {
        Optional<Course> optionalCourse = this.courseRepo.findById(id);
        if (optionalCourse.isPresent()) {
//            if (optionalCourse.get().getStatut().equals(Statut.TERMINER)) {
//                this.courseRepo.deleteById(id);
//            } else if(optionalCourse.get().getStatut().equals(Statut.DESACTIVER)) {
//                this.courseRepo.deleteById(id);
//            }
            this.courseRepo.deleteById(id);
            }
            else {
                throw new RuntimeException("Cette matiere est en cours et n'est pas encore terminee!");
            }
//        }
    }
}
