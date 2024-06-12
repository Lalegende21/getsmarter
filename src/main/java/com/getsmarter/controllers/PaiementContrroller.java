package com.getsmarter.controllers;

import com.getsmarter.entities.Paiement;
import com.getsmarter.entities.Student;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.PaiementService;
import lombok.AllArgsConstructor;
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
@RequestMapping(path = "/paiement", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaiementContrroller {

    private final PaiementService paiementService;


    @PostMapping(path = "/save-paiement")
    public ResponseEntity<?> savePaiement(@RequestBody Paiement paiement) {
        try {
            this.paiementService.savePaiement(paiement);
            return ResponseEntity.status(HttpStatus.CREATED).body(paiement);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer le paiement: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-all-paiement")
    public ResponseEntity<?> getAllPaiements() {
        try {
            List<Paiement> paiements = this.paiementService.getAllPaiement();
            return ResponseEntity.status(HttpStatus.OK).body(paiements);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste de paiements: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-paiement-frequently")
    public  ResponseEntity<?> getAllPaiementFrequently() {
        try {
            List<Paiement> paiements = this.paiementService.getRecentlyAddedPaiement();
            return ResponseEntity.status(HttpStatus.OK).body(paiements);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste de paiements: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @GetMapping(path = "/get-paiement/{id}")
    public ResponseEntity<?> getPaiementById(@PathVariable Long id) {
        try {
            Paiement paiement = this.paiementService.getPaiementById(id);
            return new ResponseEntity<>(paiement, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer le paiement: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PutMapping(path = "/update-paiement/{id}")
    public ResponseEntity<?> updatePaiement(@PathVariable Long id, @RequestBody Paiement paiement) {
        try {
            this.paiementService.updatePaiement(id, paiement);
            return new ResponseEntity<>(paiement, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de modifier le paiement: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-all-paiement")
    public ResponseEntity<?> deleteAllPaiement() {
        try {
            this.paiementService.deleteAllPaiement();
            UserResponse userResponse = new UserResponse("Tous les paiements ont ete supprimes avec succes !");
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer tous les etudiants: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @DeleteMapping(path = "/delete-paiement/{id}")
    public ResponseEntity<?> deletePaiementById(@PathVariable Long id) {
        try {
            this.paiementService.deletepaiementById(id);
            UserResponse userResponse = new UserResponse("Paiement supprime avec succes !");
            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer le paiement: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
