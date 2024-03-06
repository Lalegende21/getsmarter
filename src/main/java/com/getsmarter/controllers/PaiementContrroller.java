package com.getsmarter.controllers;

import com.getsmarter.entities.Paiement;
import com.getsmarter.services.PaiementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/paiement")
public class PaiementContrroller {

    private final PaiementService paiementService;


    @PostMapping(path = "/save-paiement")
    public ResponseEntity<String> savePaiement(@RequestBody Paiement paiement) {
        try {
            this.paiementService.savePaiement(paiement);
            return new ResponseEntity<>("Paiement register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping(path = "/get-all-paiement")
    public List<Paiement> getAllPaiement() {
        return this.paiementService.getAllPaiement();
    }



    @GetMapping(path = "/get-paiement/{id}")
    public Paiement getPaiementById(@PathVariable Long id) {
        try {
            return this.paiementService.getPaiementById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    @PutMapping(path = "/update-paiement/{id}")
    public ResponseEntity<String> updatePaiement(@PathVariable Long id, @RequestBody Paiement paiement) {
        try {
            this.paiementService.updatePaiement(id, paiement);
            return new ResponseEntity<>("Paiement update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-all-paiement")
    public ResponseEntity<String> deleteAllPaiement() {
        try {
            this.paiementService.deleteAllPaiement();
            return new ResponseEntity<>("All Paiement delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @DeleteMapping(path = "/delete-paiement/{id}")
    public ResponseEntity<String> deletePaiementById(@PathVariable Long id, @RequestBody Paiement paiement) {
        try {
            this.paiementService.deleteAllPaiement();
            return new ResponseEntity<>("All Paiement delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
