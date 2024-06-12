package com.getsmarter.controllers;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Professor;
import com.getsmarter.entities.Student;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.ProfessorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@ControllerAdvice
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/professor", produces = MediaType.APPLICATION_JSON_VALUE)
public class ProfessorController {

    private final ProfessorService professorService;


    @PostMapping(path = "/save-professor")
    public ResponseEntity<?> saveDureeCourse(@RequestBody Professor professor) {
        try {
            this.professorService.saveProfessor(professor);
            return new ResponseEntity<>(professor, HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer le professeur: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PostMapping(path = "/save-image/{id}")
    public ResponseEntity<?> uploadImageCenter(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            Professor professor = this.professorService.saveImageCenter(id, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(professor);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'image: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }



    @GetMapping(path = "/get-all-professor")
    public ResponseEntity<?> getAllProfessor() {
        try {
            List<Professor> professors = this.professorService.getAllProfessor();
            return ResponseEntity.status(HttpStatus.OK).body(professors);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des professeurs: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }


    @GetMapping(path = "/get-professor-frequently")
    public ResponseEntity<?> getAllProfessorsFrequently() {
        try {
            List<Professor> professors = this.professorService.getRecentlyAddedProfessors();
            return ResponseEntity.status(HttpStatus.OK).body(professors);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des professeurs: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }



    @GetMapping(path = "/get-professor/{id}")
    public ResponseEntity<?> getProfessorById(@PathVariable Long id) {
        try {
            Professor professor = this.professorService.getProfessorById(id);
            return new ResponseEntity<>(professor, HttpStatus.ACCEPTED);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer ce professeur: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PutMapping(path = "/update-professor/{id}")
    public ResponseEntity<?> updateProfessor(@PathVariable Long id, @RequestBody Professor professor) {
        try {
            this.professorService.updateProfessor(id, professor);
            return new ResponseEntity<>(professor, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de modifier ce professeur: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-all-professor")
    public ResponseEntity<?> deleteAllProfessor() {
        try {
            this.professorService.deleteAllProfessor();
            UserResponse userResponse = new UserResponse("Tous les professeurs ont ete supprime avec succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer tous les professeurs: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-professor/{id}")
    public ResponseEntity<?> deleteProfessorById(@PathVariable Long id) {
        try {
            this.professorService.deleteProfessorById(id);
            UserResponse userResponse = new UserResponse("Ce professeur ete supprime avec succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer ce professeur: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
