package com.getsmarter.controllers;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Formation;
import com.getsmarter.entities.Student;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.FormationService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/formation", produces = MediaType.APPLICATION_JSON_VALUE)
public class FormationController {

    private final FormationService formationService;


    //Methode pour enregistrer une formation
    @PostMapping(path = "/save-formation")
    public ResponseEntity<?> saveFormation(@RequestBody Formation formation) {
        try {
            this.formationService.saveFormation(formation);
            return new ResponseEntity<>(formation, HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer la formation: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PostMapping(path = "/save-image/{id}")
    public ResponseEntity<?> uploadImageCenter(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            Formation formation = this.formationService.saveImageCenter(id, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(formation);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'image: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }



    //Methode pour lire toutes les specialites
    @GetMapping(path = "/get-all-formation")
    public ResponseEntity<?> getAllFormations() {
        try {
            List<Formation> formations = this.formationService.getAllFormations();
            return ResponseEntity.status(HttpStatus.OK).body(formations);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des formations: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }



    @GetMapping(path = "/get-formation-frequently")
    public ResponseEntity<?> getAllFormationsFrequently() {
        try {
            List<Formation> formations = this.formationService.getRecentlyAddedFormations();
            return ResponseEntity.status(HttpStatus.OK).body(formations);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des formations: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }



    //Methode pour lire une specialite par son id
    @GetMapping(path = "/get-formation/{id}")
    public ResponseEntity<?> getFormationById(@PathVariable Long id) {
        try {
            Formation formation = this.formationService.getFormationById(id);
            return new ResponseEntity<>(formation, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer cette formation: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    //Methode pour update la formation
    @PutMapping(path = "/update-formation/{id}")
    public ResponseEntity<?> updateFormation(@PathVariable Long id, @RequestBody Formation formation) {
        try {
            this.formationService.updateFormation(id, formation);
            return new ResponseEntity<>(formation, HttpStatus.ACCEPTED);
        }catch (DataIntegrityViolationException e) {
            System.out.println(e.getMessage());
            UserResponse userResponse = new UserResponse("Vous essayer d'enregistrer une formation avec des donnees manquantes");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }catch (InvalidDataAccessApiUsageException e) {
            System.out.println(e.getMessage());
            UserResponse userResponse = new UserResponse("Veuillez renseigner tous les champs pour modifier cette formation!");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
        catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de modifier la formation: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    //Methode pour supprimer toutes les specialites
    @DeleteMapping(path = "/delete-all-formation")
    public ResponseEntity<?> deleteAllFormation() {
        try {
            this.formationService.deleteAllFrormation();
            UserResponse userResponse = new UserResponse("Toutes les formations ont ete supprimer avec succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer toutes les formations: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    //Methode pour supprimer une specialite par id
    @DeleteMapping(path = "/delete-formation/{id}")
    public ResponseEntity<?> deleteFormationById(@PathVariable Long id) {
        try {
            this.formationService.deleteFormationById(id);
            UserResponse userResponse = new UserResponse("Formation supprimee avec succes!");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (DataIntegrityViolationException ex) {
            UserResponse userResponse = new UserResponse("Erreur d'intégrité des données.\n"
                    +"Impossible de supprimer la formation car elle est liée à des etudiants");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer cette formation: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
