package com.getsmarter.controllers;


import com.getsmarter.entities.TypePaiement;
import com.getsmarter.services.TypePaiementService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/type-paiement")
public class TypePaiementController {

    private final TypePaiementService typePaiementService;


    @PostMapping(path = "/save-type-paiement")
    public ResponseEntity<String> saveTypePaiement(@RequestBody TypePaiement typePaiement) {
        try {
            this.typePaiementService.saveTypePaiement(typePaiement);
            return new ResponseEntity<>("Type de paiement register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping(path = "/get-all-type-paiement")
    public List<TypePaiement> getAllTypePaiement() {
        return this.typePaiementService.getAllTypePaiement();
    }



    @GetMapping(path = "/get-type-paiement/{id}")
    public TypePaiement getAllTypePaiementById(@PathVariable Long id) {
        try {
            return this.typePaiementService.getTypePaiementById(id);
        }catch (Exception e){
            System.out.println(e);
            return null;
        }
    }



    @PutMapping(path = "/update-type-paiement")
    public ResponseEntity<String> updateTypePaiement(@PathVariable Long id, @RequestBody TypePaiement typePaiement) {
        try {
            this.typePaiementService.updateTypePaiement(id, typePaiement);
            return new ResponseEntity<>("Type de paiement update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-all-type-paiement")
    public ResponseEntity<String> deleteAllTypePaiement() {
        try {
            this.typePaiementService.deleteAllTypePaiement();
            return new ResponseEntity<>("All type de paiement delete sucessfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-type-paiement/{id}")
    public ResponseEntity<String> deleteTypePaiementById(@PathVariable Long id) {
        try {
            this.typePaiementService.deleteAllTypePaiement();
            return new ResponseEntity<>("Type de paiement with id: " +id+ " delete sucessfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
