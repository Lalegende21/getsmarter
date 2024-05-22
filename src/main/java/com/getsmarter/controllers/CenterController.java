package com.getsmarter.controllers;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Image;
import com.getsmarter.entities.Student;
import com.getsmarter.response.UploadImageResult;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.CenterService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@AllArgsConstructor
@ControllerAdvice
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/center", produces = MediaType.APPLICATION_JSON_VALUE)
public class CenterController {

    private final CenterService centerService;


    //Methode pour enregistrer un centre
    @PostMapping(path = "/save-center")
    public ResponseEntity<?> saveCenter(@RequestBody Center center) {
        try {
            this.centerService.saveCenter(center);
            return new ResponseEntity<>(center, HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer le campus: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PostMapping(path = "/save-image/{id}")
    public ResponseEntity<?> uploadImageCenter(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            Center center = this.centerService.saveImageCenter(id, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(center);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'image: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }



    //Methode pour lire tous les centres
    @GetMapping(path = "/get-all-center")
    public List<Center> getAllCenter() {
        return this.centerService.getAllCenter();
    }



    @GetMapping(path = "/get-center-frequently")
    public List<Center> getAllStudentFrequently() {
        return this.centerService.getRecentlyAddedCenters();
    }



    //Methode pour lire les centres par id
    @GetMapping(path = "/get-center/{id}")
    public ResponseEntity<?> getCenterById(@PathVariable Long id) {
        try {
            Center center = this.centerService.getCenterById(id);
            return new ResponseEntity<>(center, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer ce campus: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    //Methode pour lire les centres par nom
    @GetMapping(path = "/get-center-name/{name}")
    public ResponseEntity<?> getCenterByName(@PathVariable String name) {
        try {
            Center center = this.centerService.getCenterByName(name);
            return new ResponseEntity<>(center, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer ce campus: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    //Methode pour update les centres
    @PutMapping(path = "/update-center/{id}")
    public ResponseEntity<?> updateCenter(@PathVariable Long id, @RequestBody Center center) {
        try {
            this.centerService.updateCenter(id, center);
            return new ResponseEntity<>(center, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de modifier ce campus: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    //Methode pour supprimer tous les centres
    @DeleteMapping(path = "/delete-all-center")
    public ResponseEntity<?> deleteAllCenter() {
        try {
            this.centerService.deleteAllCenter();
            UserResponse userResponse = new UserResponse("Tous les campus ont ete supprime avec succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer tous les campus: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    //Methode pour supprimer les centres par leurs id
    @DeleteMapping(path = "/delete-center/{id}")
    public ResponseEntity<?> deleteCenterById(@PathVariable Long id) {
        try {
            this.centerService.deleteCenterById(id);
            UserResponse userResponse = new UserResponse("Campus supprime avec succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            UserResponse userResponse = new UserResponse("Erreur d'intégrité des données.\n"
                    +"Impossible de supprimer le centre car il est lié à d'autres donnees'");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
        catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer ce campus: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
