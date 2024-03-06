package com.getsmarter.controllers;

import com.getsmarter.entities.Center;
import com.getsmarter.services.CenterService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/center")
public class CenterController {

    private final CenterService centerService;


    //Methode pour enregistrer un centre
    @PostMapping(path = "/save-center")
    public ResponseEntity<String> saveCenter(@RequestBody Center center) {
        try {
            this.centerService.saveCenter(center);
            return new ResponseEntity<>("Center register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour lire tous les centres
    @GetMapping(path = "/get-all-center")
    public List<Center> getAllCenter() {
        return this.centerService.getAllCenter();
    }



    //Methode pour lire les centres par id
    @GetMapping(path = "/get-center/{id}")
    public Center getCenterById(@PathVariable Long id) {
        try {
            return this.centerService.getCenterById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    //Methode pour update les centres
    @PutMapping(path = "/update-center/{id}")
    public ResponseEntity<String> updateCenter(@PathVariable Long id, @RequestBody Center center) {
        try {
            this.centerService.updateCenter(id, center);
            return new ResponseEntity<>("Center updated successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer tous les centres
    @DeleteMapping(path = "/delete-all-center")
    public ResponseEntity<String> deleteAllCenter() {
        try {
            this.centerService.deleteAllCenter();
            return new ResponseEntity<>("All center was successfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    //Methode pour supprimer les centres par leurs id
    @DeleteMapping(path = "delete-center/{id}")
    public ResponseEntity<String> deleteCenterById(@PathVariable Long id) {
        try {
            this.centerService.deleteCenterById(id);
            return new ResponseEntity<>("Center with id "+id+ " was successfully delete !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
