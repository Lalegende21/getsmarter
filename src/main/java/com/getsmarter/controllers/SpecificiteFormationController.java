package com.getsmarter.controllers;

import com.getsmarter.entities.SpecificiteFormation;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.SpecificiteFormationService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/specificite-formation", produces = MediaType.APPLICATION_JSON_VALUE)
public class SpecificiteFormationController {

    private final SpecificiteFormationService specificiteFormationService;


    @PostMapping(path = "/save-specificite-formation")
    public ResponseEntity<?> saveCodeFormation(@RequestBody SpecificiteFormation specificiteFormation) {
        try {
            this.specificiteFormationService.saveCode(specificiteFormation);
            return new ResponseEntity<>(specificiteFormation, HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer cette specificite: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-all-specificite-formation")
    public ResponseEntity<?> getAllCodeFormations() {
        try {
            List<SpecificiteFormation> specificiteFormations = this.specificiteFormationService.getAllCode();
            return new ResponseEntity<>(specificiteFormations, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des specificites: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-specificite-formation/{id}")
    public ResponseEntity<?> getCodeFormationById(@PathVariable Long id) {
        try {
            SpecificiteFormation specificiteFormation = this.specificiteFormationService.getCodeById(id);
            return new ResponseEntity<>(specificiteFormation, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer cette specificite: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PutMapping(path = "/update-specificite-formation/{id}")
    public ResponseEntity<?> updateCodeFormation(@PathVariable Long id, @RequestBody SpecificiteFormation specificiteFormation) {
        try {
            this.specificiteFormationService.updateCode(id, specificiteFormation);
            return new ResponseEntity<>(specificiteFormation, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            UserResponse userResponse = new UserResponse("Impossible de modifier cette specificite: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-all-specificite-formation")
    public ResponseEntity<?> deleteAllCodeFormation() {
        try {
            this.specificiteFormationService.deleteAllCode();
            UserResponse userResponse = new UserResponse("Tous les specificites ont supprime ave succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer toutes les specificites: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-specificite-formation/{id}")
    public ResponseEntity<?> deleteCodeFormationById(@PathVariable Long id) {
        try {
            this.specificiteFormationService.deleteCodeById(id);
            UserResponse userResponse = new UserResponse("La specificite a ete supprime ave succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.ACCEPTED);
        }catch (DataIntegrityViolationException ex) {
            UserResponse userResponse = new UserResponse("Erreur d'intégrité des données.\n"
                    +"Impossible de supprimer la specificite car elle est liée à des formations");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer la specificite: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
