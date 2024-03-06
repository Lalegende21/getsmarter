package com.getsmarter.controllers;

import com.getsmarter.entities.TypeTutor;
import com.getsmarter.services.TypeTutorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/type-tutor")
public class TypeTutorController {

    private final TypeTutorService typeTutorService;


    //Methode pour enregistrer un type de tuteur
    @PostMapping(path = "/save-type-tutor")
    public ResponseEntity<String> saveTypeTutor(@RequestBody TypeTutor typeTutor) {
        try {
            this.typeTutorService.saveTypteTutor(typeTutor);
            return new ResponseEntity<>("Type tutor register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour recuperer tous les types de tuteur
    @GetMapping(path = "/get-all-type-tutor")
    public List<TypeTutor> getAllTypeTutor() {
        return this.typeTutorService.getAllTypeTutor();
    }



    //Methode pour recuperer un type de tuteur par son id
    @GetMapping(path = "/get-type-tutor/{id}")
    public TypeTutor getTypeTutorById(@PathVariable Long id) {
        try {
            return this.typeTutorService.getTypeTutorById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    //Methode pour update un type de tuteur
    @PutMapping(path = "/update-type-tutor/{id}")
    public ResponseEntity<String> updateTypeTutor(@PathVariable Long id, @RequestBody TypeTutor typeTutor) {
        try {
            this.typeTutorService.updateTypeTutor(id, typeTutor);
            return new ResponseEntity<>("Type tutor update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer tous les types de tuteur
    @DeleteMapping(path = "/delete-all-type-tutor")
    public ResponseEntity<String> deleteAllTypeTutor() {
        try {
            this.typeTutorService.deleteAllTypeTutor();
            return new ResponseEntity<>("All type tutor delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Mehtode pour supprimer un type de tuteur par son id
    @DeleteMapping(path = "/delete-type-tutor/{id}")
    public ResponseEntity<String> deleteTypeTutorById(@PathVariable Long id) {
        try {
            this.typeTutorService.deleteTypeTutorById(id);
            return new ResponseEntity<>("Type tutor with id "+id+" delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
