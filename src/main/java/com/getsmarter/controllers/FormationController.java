package com.getsmarter.controllers;

import com.getsmarter.entities.Formation;
import com.getsmarter.services.FormationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/formation")
public class FormationController {

    private final FormationService formationService;


    //Methode pour enregistrer une formation
    @PostMapping(path = "/save-formation")
    public ResponseEntity<String> saveFormation(@RequestBody Formation formation) {
        try {
            this.formationService.saveFormation(formation);
            return new ResponseEntity<>("Formation enregistrer avec succes !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour lire toutes les specialites
    @GetMapping(path = "/get-all-formation")
    public List<Formation> getAllFormations() {
        return this.formationService.getAllFormations();
    }



    //Methode pour lire une specialite par son id
    @GetMapping(path = "/get-formation/{id}")
    public Formation getFormationById(@PathVariable Long id) {
        try {
            return this.formationService.getFormationById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    //Methode pour update la formation
    @PutMapping(path = "/update-formation/{id}")
    public ResponseEntity<String> updateFormation(@PathVariable Long id, @RequestBody Formation formation) {
        try {
            this.formationService.updateFormation(id, formation);
            return new ResponseEntity<>("Speciality update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer toutes les specialites
    @DeleteMapping(path = "/delete-all-formation")
    public ResponseEntity<String> deleteAllFormation() {
        try {
            this.formationService.deleteAllFrormation();
            return new ResponseEntity<>("All speciality was successfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer une specialite par id
    @DeleteMapping(path = "/delete-formation/{id}")
    public ResponseEntity<String> deleteFormationById(@PathVariable Long id) {
        try {
            this.formationService.deleteFormationById(id);
            return new ResponseEntity<>("Speciality with id: "+id+ " was sucessfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
