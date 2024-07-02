package com.getsmarter.services;

import com.getsmarter.dto.AmountDto;
import com.getsmarter.entities.*;
import com.getsmarter.mails.EmailService;
import com.getsmarter.repositories.FormationRepo;
import com.getsmarter.repositories.ImageRepo;
import com.getsmarter.repositories.StudentRepo;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Year;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;

    private final FormationService formationService;

    private final FormationRepo formationRepo;

    private final ImageService imageService;

    private final ImageRepo imageRepo;

    private final EmailService emailService;


    //Methode pour enregistrer un student
    public void saveStudent(Student student) {

        // On verifie si l'email contient le symbole @
//        if (!student.getEmail().contains("@")){
//            throw new RuntimeException("Votre email est invalide!");
//        }

        //On verifie si l'email contient un .
//        if (!student.getEmail().contains(".")){
//            throw new RuntimeException("Votre email est invalide!");
//        }

        //On verifie si un utilisateur avec l'email donnee existe deja
        Optional<Student> optionalStudent = this.studentRepo.findByEmail(student.getEmail());
        if (optionalStudent.isPresent()){
            throw new RuntimeException("Votre email est déjà utilisée!");
        }

        //On verifie si un utilisateur avec le numero de telephone donnee existe deja
        Optional<Student> optionalStudentPhoneNumber = this.studentRepo.findByPhonenumber(student.getPhonenumber());
        if (optionalStudentPhoneNumber.isPresent()) {
            throw new RuntimeException("Votre numero de telephone est déjà utilisé!");
        }

        //On recupere d'abord le code de la formation
        Long id = student.getFormation().getId();
        Formation formation = this.formationService.getFormationById(id);
        String code = formation.getSpecificiteFormation().getCode().toUpperCase();

        //On attribue un matricule au student
        String matricule = this.generateMatricule(code);
        student.setMatricule(matricule);

        //On verifie si un utilisateur avec le matricule donnee existe deja
        Optional<Student> optionalStudentMatricule = this.studentRepo.findByMatricule(student.getMatricule());
        if (optionalStudentMatricule.isPresent()) {
            throw new RuntimeException("Ce matricule est deja utilise !");
        }


        String specialite = formation.getSpecialite().toUpperCase();
        String duree = formation.getPeriode();
        String email = student.getEmail();
        String subject = "Confirmation d'enregistrement en tant qu'etudiant chez Getsmarter ! " +"\uD83C\uDF89";
        String text = "Félicitations 🎉 Vous êtes maintenant enregistré comme étudiant(e) de Getsmarter en spécialité "
                +specialite+ " Pour une durée de "
                +duree+ " mois avec de 2 mois de stage compris."
                +" ! 📚 Profitez pleinement de cette nouvelle aventure! 🌟";

        if (!student.getEmail().isEmpty()) {
            this.emailService.sendEmail(email, subject, text);
        }

        student.setMontantTotal(formation.getPrice());
        student.setCreated_at(LocalDateTime.now());
        this.studentRepo.save(student);
    }



    @Transactional
    public Student saveImageStudent(Long id, MultipartFile imageFile) throws IOException {
        // Vérifier la taille du fichier image
        if (imageFile.getSize() > 5 * 1024 * 1024) {
            throw new RuntimeException("Le poids de l'image ne doit pas depasser 5MB.");
        }

        // Définir l'URL de l'image sur l'entité Center
        Optional<Student> optionalStudent = this.studentRepo.findById(id);
        if (optionalStudent.isEmpty()) {
            throw new EntityNotFoundException("Aucun etudiant avec cet identifiant trouve !");
        }

        // Vérifier si une image existe déjà pour ce center
        Image existingImage = this.imageRepo.findByStudent(optionalStudent.get());
        if (existingImage != null) {
            // Supprimer l'image existante
            this.imageRepo.delete(existingImage);
        }

        // Enregistrer la nouvelle image
        Image newImage = this.imageService.uploadImageToFolder(imageFile);
        newImage.setStudent(optionalStudent.get());
        this.imageRepo.save(newImage);

        // Enregistrer le center avec l'URL de la nouvelle image
        optionalStudent.get().setImage(newImage.getFilePath());
        return this.studentRepo.save(optionalStudent.get());
    }



    //Methode pour generer le matricule du student
    private String generateMatricule(String code) {

        // Obtenir l'année courante
        int currentYear = Year.now().getValue();

        // Générer un UUID
        UUID uuid = UUID.randomUUID();


        // Extraire les six premiers caractères de l'UUID
        String uuidSubstring = uuid.toString().substring(0, 4).toUpperCase();

        // Concaténer les quatre premiers caractères de l'année courante avec les six caractères de l'UUID
        String matricule = String.format("%s%04d%s%s", "G-", currentYear, code, uuidSubstring);
        return matricule;
    }



    //Methode pour recuperer tous les students
    public List<Student> getAllStudent() {
        //Afficher les resultats de la base de donne par ordre decroissant
        Sort sort = Sort.by(Sort.Direction.DESC, "id");

        return this.studentRepo.findAll(sort);
    }

    public List<Paiement> getPaiementByStudent(Long id) {
        Student student = this.studentRepo.findById(id).orElseThrow(() -> new RuntimeException("Aucun etudiant avec cet identifiant trouve !"));
        return student.getPaiement();
    }


    //Methode pour recuperer les etudiants ajoutes recemment (1 derniers jours)
    public List<Student> getRecentlyAddedStudents() {
        // Définir la date de début pour récupérer les étudiants ajoutés récemment (par exemple, les 1 derniers jours)
        // Logique pour déterminer la date de début appropriée (1 jours avant la date actuelle)
        LocalDateTime startDate = LocalDate.now().minus(1, ChronoUnit.DAYS).atStartOfDay();

        return this.studentRepo.findRecentlyAddedStudents(startDate);
    }


    //Methode pour accorder une reduction a l'etudiant
    public Student getReduction(Long id, AmountDto amount) {
        Student student = this.studentRepo.findById(id).orElseThrow(() -> new RuntimeException("Aucun etudiant avec cet identifiant trouve !"));

        BigDecimal newAmountAPayer = BigDecimal.ZERO;
        BigDecimal newAmountTotal = student.getMontantTotal().subtract(amount.getAmount());

        if (Objects.isNull(student.getMontantRestantaPayer())) {
            student.setMontantRestantaPayer(BigDecimal.ZERO);
        } else {
            if(Objects.isNull(student.getMontantPaye())) {
                student.setMontantRestantaPayer(BigDecimal.ZERO);
            } else {
                newAmountAPayer = student.getMontantRestantaPayer().subtract(amount.getAmount());
            }
        }

        student.setMontantTotal(newAmountTotal);
        student.setMontantRestantaPayer(newAmountAPayer);

        return this.studentRepo.save(student);
    }


    //Methode pour recuperer un student par son id
    public Student getStudentById(Long id) {
        Optional<Student> optionalStudent = this.studentRepo.findById(id);
        return optionalStudent.orElseThrow(() -> new RuntimeException("Aucun etudiant trouve avec cet identifiant: "+id));
    }


    //Methode pour recupere un student par matricule
    public Student getStudentByMatricule(String matricule) {
        Optional<Student> optionalStudent = this.studentRepo.findByMatricule(matricule);
        return optionalStudent.orElseThrow(() -> new RuntimeException("Aucun etudiant trouve avec ce matricule: "+matricule));
    }


    //Methode pour recuperer un student par son nom complet
    public Optional<Student> findStudentByFullName(String fullName) {
        String[] names = fullName.split(" ");
        if (names.length < 2) {
            throw new IllegalArgumentException("Le nom complet doit être composé d'au moins un prénom et un nom de famille.");
        }
        String firstname = names[0];
        String lastname = names[1];
        return studentRepo.findByFirstnameAndLastname(firstname, lastname);
    }


    //Methode pour faire a mise a jour des donnes d'un student
    public void updateStudent(Long id, Student student) {
        Student updateStudent = this.getStudentById(id);

        if (updateStudent.getId().equals(student.getId())) {
            updateStudent.setFirstname(student.getFirstname());
            updateStudent.setLastname(student.getLastname());
            updateStudent.setEmail(student.getEmail());
            updateStudent.setPhonenumber(student.getPhonenumber());
            updateStudent.setCni(student.getCni());
            updateStudent.setSexe(student.getSexe());
            updateStudent.setDob(student.getDob());
            updateStudent.setCenter(student.getCenter());
            updateStudent.setFormation(student.getFormation());
            updateStudent.setSession(student.getSession());

            //On recupere le montant de la formation
            Optional<Formation> optionalFormation = this.formationRepo.findById(student.getFormation().getId());
            if (optionalFormation.isEmpty()) {
                throw new RuntimeException("Aucune formation avec cet identifiant trouve !");
            }
            BigDecimal price = optionalFormation.get().getPrice();
            updateStudent.setMontantTotal(price);

            //On recupere le montant deja paye
            Optional<Student> studentMontantPaye = this.studentRepo.findById(id);
            if (studentMontantPaye.isEmpty()) {
                throw new RuntimeException("Aucun etudiant trouve avec cet identifiant !");
            }
            BigDecimal montantPaye = studentMontantPaye.get().getMontantPaye();

            updateStudent.setHoraire(student.getHoraire());
            if(montantPaye == null) {
                updateStudent.setMontantRestantaPayer(null);
            } else {
                updateStudent.setMontantRestantaPayer(price.subtract(montantPaye));
            }
            System.out.println(student.getFormation());
            this.studentRepo.save(updateStudent);
        } else {
            throw new RuntimeException("Incoherence entre l'id fourni et l'id de l'etudiant a modifie !");
        }
    }



    //Methode pour supprimer tous les students
    public void deleteAllStudent() {
        this.studentRepo.deleteAll();
    }



    //Mehtode pour supprimer un student par id
    public void deleteStudentById(Long id) {
        this.studentRepo.deleteById(id);
    }
}
