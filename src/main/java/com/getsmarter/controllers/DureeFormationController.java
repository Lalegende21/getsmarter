package com.getsmarter.controllers;

import com.getsmarter.entities.DureeFormation;
import com.getsmarter.services.DureeFormationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/duree")
public class DureeFormationController {

    private final DureeFormationService dureeFormationService;


    //Methode pour enregistrer une duree
    @PostMapping(path = "/save-duree")
    public ResponseEntity<String> saveDuree(@RequestBody DureeFormation dureeFormation) {
        try {
            this.dureeFormationService.saveDuree(dureeFormation);
            return new ResponseEntity<>("Duree de formation register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour lire toutes les durees
    @GetMapping(path = "/get-all-duree")
    public List<DureeFormation> getAllDuree() {
        return this.dureeFormationService.getAllDuree();
    }



    //Methode pour lire une dure par id
    @GetMapping(path = "get-dure/{id}")
    public DureeFormation getDureeById(@PathVariable Long id) {
        try {
            return this.dureeFormationService.getDureeById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    //Methode pour update une duree
    @PutMapping(path = "/update-duree/{id}")
    public ResponseEntity<String> updateDuree(@PathVariable Long id, @RequestBody DureeFormation dureeFormation) {
        try {
            this.dureeFormationService.updateDuree(id, dureeFormation);
            return new ResponseEntity<>("Duree was successfully update !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer toutes les durees
    @DeleteMapping(path = "/delete-all-duree")
    public ResponseEntity<String> deleteAllDuree() {
        try {
            this.dureeFormationService.deleteAllDuree();
            return new ResponseEntity<>("All duree was successfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer une duree par id
    @DeleteMapping(path = "/delete-duree/{id}")
    public ResponseEntity<String> deleteDureeById(@PathVariable Long id) {
        try {
            this.dureeFormationService.deleteById(id);
            return new ResponseEntity<>("Duree with id: " +id+ " was successfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.OK);
        }
    }
}
