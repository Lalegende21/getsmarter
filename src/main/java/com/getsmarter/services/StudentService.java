package com.getsmarter.services;

import com.getsmarter.entities.Formation;
import com.getsmarter.entities.Student;
import com.getsmarter.mails.EmailService;
import com.getsmarter.repositories.StudentRepo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;

    private final FormationService formationService;

    private final EmailService emailService;


    //Methode pour enregistrer un student
    public void saveStudent(Student student) {

        // On verifie si l'email contient le symbole @
        if (!student.getEmail().contains("@")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si l'email contient un .
        if (!student.getEmail().contains(".")){
            throw new RuntimeException("Votre email est invalide!");
        }

        //On verifie si un utilisateur avec l'email donnee existe deja
        Optional<Student> optionalStudent = this.studentRepo.findByEmail(student.getEmail());
        if (optionalStudent.isPresent()){
            throw new RuntimeException("Votre email est déjà utilisée!");
        }

        //On verifie si un utilisateur avec le numero de telephone donnee existe deja
        Optional<Student> optionalStudentPhoneNumber = this.studentRepo.findByPhonenumber(student.getPhonenumber());
        if (optionalStudentPhoneNumber.isPresent()) {
            throw new RuntimeException("Votre numero de telephone est déjà utilisée!");
        }

        //On attribue un matricule au student
        //On recupere d'abord le code de la formation
        Long id = student.getFormation().getId();
        Formation formation = this.formationService.getFormationById(id);
        String code = formation.getCodeFormation().getCode().toUpperCase();

        String matricule = this.generateMatricule(code);
        student.setMatricule(matricule);

        //On verifie si un utilisateur avec le matricule donnee existe deja
        Optional<Student> optionalStudentMatricule = this.studentRepo.findByMatricule(student.getMatricule());
        if (optionalStudentMatricule.isPresent()) {
            throw new RuntimeException("Ce matricule est deja utilise !");
        }


        String specialite = formation.getName().toUpperCase();
        String duree = formation.getDuree().getPeriode();
        String email = student.getEmail();
        String subject = "Confirmation d'enregistrement en tant qu'administrateur ! " +"\uD83C\uDF89";
        String text = "Félicitations 🎉 Vous êtes maintenant enregistré comme étudiant de Getsmarter en spécialité "
                +specialite+ " Pour une durée de "
                +duree
                +" ! 📚 Profitez pleinement de cette nouvelle aventure! 🌟";
        this.emailService.sendEmail(email, subject, text);

        student.setCreated_at(LocalDateTime.now());
        this.studentRepo.save(student);
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
        return this.studentRepo.findAll();
    }


    //Methode pour recuperer un student par son id
    public Student getStudentById(Long id) {
        Optional<Student> optionalStudent = this.studentRepo.findById(id);
        return optionalStudent.orElseThrow(() -> new RuntimeException("Student with id: "+id+ " not found !"));
    }


    //Methode pour recupere un student par matricule
    public Student getStudentByMatricule(String matricule) {
        Optional<Student> optionalStudent = this.studentRepo.findByMatricule(matricule);
        return optionalStudent.orElseThrow(() -> new RuntimeException("Student with matricule: " +matricule+ " not found !"));
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
            updateStudent.setAdmin(student.getAdmin());
            updateStudent.setCenter(student.getCenter());
            updateStudent.setFormation(student.getFormation());
            updateStudent.setTutor(student.getTutor());
            updateStudent.setMontantPaye(student.getMontantPaye());
            updateStudent.setMontantRestantaPayer(student.getMontantRestantaPayer());
            this.studentRepo.save(updateStudent);
        } else {
            throw new RuntimeException("Something went wrong !");
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
