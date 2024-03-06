package com.getsmarter.controllers;

import com.getsmarter.dto.FactureDto;
import com.getsmarter.services.FactureService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/facture")
public class FactureController {

    private final FactureService factureService;


    @PostMapping(path = "/save-facture")
    public ResponseEntity<String> saveFacture(@RequestBody FactureDto factureDto) {
        try {
            this.factureService.saveFacture(factureDto.getStudentId(), factureDto.getPaiementID());
            return new ResponseEntity<>("Facture save successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
