package com.getsmarter.controllers;

import com.getsmarter.entities.*;
import com.getsmarter.response.UploadImageResult;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.ImageService;
import com.getsmarter.services.StudentService;
import lombok.AllArgsConstructor;
import org.eclipse.angus.mail.util.MailConnectException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@AllArgsConstructor
@ControllerAdvice
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/student", produces = MediaType.APPLICATION_JSON_VALUE)
public class StudentController {

    private final StudentService studentService;

    private final ImageService imageService;


    @PostMapping(path = "/save-student")
    public ResponseEntity<?> saveStudent(@RequestBody Student student) {
        try {
            this.studentService.saveStudent(student);
            return new ResponseEntity<>(student, HttpStatus.CREATED);
        }catch (MailException mailException) {
            System.out.println(mailException.getMessage());
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'etudiant car nous n'avons pas pu envoyer un mail a l'etudiant, verifier l'adresse mail de votre destinataire ou votre connexion internet.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
        catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'etudiant: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @PostMapping(path = "/save-image/{id}")
    public ResponseEntity<?> uploadImageStudent(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            Student student = this.studentService.saveImageStudent(id, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(student);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'image: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }


    @GetMapping(path = "/get-all-student")
    public ResponseEntity<?> getAllStudents() {
        try {
            List<Student> students = this.studentService.getAllStudent();
            return new ResponseEntity<>(students, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des etudiants: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }


    @GetMapping(path = "/get-student-frequently")
    public ResponseEntity<?> getAllStudentsFrequently() {
        try {
            List<Student> students = this.studentService.getRecentlyAddedStudents();
            return new ResponseEntity<>(students, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des etudiants: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }


    @GetMapping("/get-student-paiement/{id}")
    public List<Paiement> getPaymentsByStudent(@PathVariable Long id) {
        return studentService.getPaiementByStudent(id);
    }



    @GetMapping(path = "/get-student/{id}")
    public ResponseEntity<?> getStudentById(@PathVariable Long id) {
        try {
            Student student = this.studentService.getStudentById(id);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer l'etudiant': "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @GetMapping(path = "/get-student-matricule/{matricule}")
    public ResponseEntity<?> getStudentByMatricule(@PathVariable String matricule) {
        try {
            Student student = this.studentService.getStudentByMatricule(matricule);
            return new ResponseEntity<>(student, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer l'etudiant': "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping("/get-student-name/{fullName}")
    public ResponseEntity<Student> searchStudentByFullName(@PathVariable String fullName) {
        Optional<Student> student = studentService.findStudentByFullName(fullName);
        if (student.isPresent()) {
            return ResponseEntity.ok(student.get());
        } else {
            return ResponseEntity.notFound().build();
        }
    }



    @PutMapping(path = "/update-student/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            this.studentService.updateStudent(id, student);
            return new ResponseEntity<>(student, HttpStatus.ACCEPTED);
        }catch (InvalidDataAccessApiUsageException invalidDataAccessApiUsageException) {
            System.out.println(invalidDataAccessApiUsageException.getMessage());
            UserResponse userResponse = new UserResponse("Impossible de modifier l'etudiant: "+invalidDataAccessApiUsageException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
            UserResponse userResponse = new UserResponse("Impossible de modifier l'etudiant: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-all-student")
    public ResponseEntity<?> deleteAllStudent() {
        try {
            this.studentService.deleteAllStudent();
            UserResponse userResponse = new UserResponse("tous les etudiants ont ete supprimes avec succes !");
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer les etudiants: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-student/{id}")
    public ResponseEntity<?> deleteStudentById(@PathVariable Long id) {
        try {
            this.studentService.deleteStudentById(id);
            UserResponse userResponse = new UserResponse("Etudiant supprime avec succes !");
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer l' etudiant: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
