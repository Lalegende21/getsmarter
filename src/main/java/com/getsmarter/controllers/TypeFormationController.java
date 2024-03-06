package com.getsmarter.controllers;

import com.getsmarter.entities.TypeFormation;
import com.getsmarter.services.TypeFormationService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/type-formation")
public class TypeFormationController {

    private final TypeFormationService typeFormationService;


    //Methode pour enregistrer un type de formation
    @PostMapping(path = "/save-type-formation")
    public ResponseEntity<String> saveTypeFormation(@RequestBody TypeFormation typeFormation) {
        try {
            this.typeFormationService.saveTypeFormation(typeFormation);
            return new ResponseEntity<>("Type de formation save successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour lire tous les types de formations
    @GetMapping(path = "/get-all-type-formation")
    public List<TypeFormation> getAllTypeFormation() {
        return this.typeFormationService.getAllTypeFormation();
    }



    //Methode pour lire un type par son id
    @GetMapping(path = "/get-type/{id}")
    public TypeFormation getTypeFormationById(@PathVariable Long id) {
        try {
            return this.typeFormationService.getTypeFormationById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    //Methode pour update un type de formation
    @PutMapping(path = "/update-type-formation/{id}")
    public ResponseEntity<String> updateTypeFormation(@PathVariable Long id, @RequestBody TypeFormation typeFormation) {
        try {
            this.typeFormationService.updateTypeFormation(id, typeFormation);
            return new ResponseEntity<>("Type de formation update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer tous les types de formations
    @DeleteMapping(path = "/delete-all-type-formation")
    public ResponseEntity<String> deleteAllTypeFormation() {
        try {
            this.typeFormationService.deleteAllType();
            return new ResponseEntity<>("All type was successfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer un type de formation par id
    @DeleteMapping(path = "/delete-type-formation/{id}")
    public ResponseEntity<String> deleteTypeFormationById(@PathVariable Long id) {
        try {
            this.typeFormationService.deleteTypeById(id);
            return new ResponseEntity<>("Type formation with id: " +id+ " delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
