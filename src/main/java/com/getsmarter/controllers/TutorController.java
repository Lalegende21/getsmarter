package com.getsmarter.controllers;

import com.getsmarter.entities.Session;
import com.getsmarter.entities.Student;
import com.getsmarter.entities.Tutor;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.TutorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@ControllerAdvice
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/tutor", produces = MediaType.APPLICATION_JSON_VALUE)
public class TutorController {

    private final TutorService tutorService;


    @PostMapping(path = "/save-tutor")
    public ResponseEntity<?> saveTutor(@RequestBody Tutor tutor) {
            try {
                this.tutorService.saveTutor(tutor);
                return new ResponseEntity<>(tutor, HttpStatus.CREATED);
            }catch (NullPointerException e) {
                UserResponse userResponse = new UserResponse("L'etudiant n'a pas pu etre affecte a ce tuteur!");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
            }
            catch (Exception e) {
                System.out.println(e);
                UserResponse userResponse = new UserResponse("Impossible d'enregistrer le tuteur: "+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
            }
    }



    @PostMapping(path = "/add-student-to-tutor/{id}")
    public ResponseEntity<?> addStudentToTutor(@PathVariable Long id, @RequestBody Student student) {
        try {
            this.tutorService.addStudentTotutor(id, student);
            UserResponse userResponse = new UserResponse("Etudiant ajoute au tuteur !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'ajouter l'etudiant au tuteur: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PostMapping(path = "/remove-student/{studentID}/to-tutor/{id}")
    public ResponseEntity<?> removeStudentToTutor(@PathVariable Long studentID, @PathVariable Long id) {
        try {
            this.tutorService.removeStudentToTutor(id, studentID);
            UserResponse userResponse = new UserResponse("Etudiant supprime du tuteur !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (IllegalArgumentException illegalArgumentException) {
            System.out.println(illegalArgumentException);
            UserResponse userResponse = new UserResponse(illegalArgumentException.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
        catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer l'etudiant au tuteur: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @GetMapping(path = "/get-all-tutor")
    public ResponseEntity<?> getAllTutors() {
        try {
            List<Tutor> tutors = this.tutorService.getAllTutor();
            return new ResponseEntity<>(tutors, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des tuteurs: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-all-student-tutor/{id}")
    public ResponseEntity<?> getAllStudentByTutor(@PathVariable Long id) {
        try {
            Tutor tutor = this.tutorService.getTutorById(id);

            List<Student> students = tutor.getStudents();
            return new ResponseEntity<>(students, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @GetMapping(path = "/get-tutor-frequently")
    public List<Tutor> getAllTutorFrequently() {
        return this.tutorService.getRecentlyAddedTutors();
    }
    public ResponseEntity<?> getAllTutorsFrequently() {
        try {
            List<Tutor> tutors = this.tutorService.getRecentlyAddedTutors();
            return new ResponseEntity<>(tutors, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des tuteurs: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-tutor/{id}")
    public Tutor getTutorById(@PathVariable Long id) {
            return this.tutorService.getTutorById(id);
    }



    @PutMapping(path = "/update-tutor/{id}")
    public ResponseEntity<?> updateTutor(@PathVariable Long id, @RequestBody Tutor tutor) {
            try {
                this.tutorService.updateTutor(id, tutor);
                return new ResponseEntity<>(tutor, HttpStatus.ACCEPTED);
            }catch (Exception e) {
                UserResponse userResponse = new UserResponse("Impossible de modifier l'etudiant: "+e.getMessage());
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
            }
    }



    @DeleteMapping(path = "/delete-all-tutor")
    public ResponseEntity<?> deleteAllTutor() {
        try {
            this.tutorService.deleteAllTutor();
            UserResponse userResponse = new UserResponse("tous les tuteurs ont ete supprimes avec succes !");
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer les tuteurs: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-tutor/{id}")
    public ResponseEntity<?> deleteTutorById(@PathVariable Long id) {
        try {
            this.tutorService.deleteTutorById(id);
            UserResponse userResponse = new UserResponse("Tuteur supprime avec succes !");
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer le tuteur: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
