package com.getsmarter.controllers;

import com.getsmarter.entities.Tutor;
import com.getsmarter.services.TutorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/tutor")
public class TutorController {

    private final TutorService tutorService;


    @PostMapping(path = "/save-tutor")
    public ResponseEntity<String> saveTutor(@RequestBody Tutor tutor) {
        try {
            this.tutorService.saveTutor(tutor);
            return new ResponseEntity<>("Tutor register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+ e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping(path = "/get-all-tutor")
    public List<Tutor> getAllTutor() {
        return this.tutorService.getAllTutor();
    }



    @GetMapping(path = "/get-tutor/{id}")
    public Tutor getTutorById(@PathVariable Long id) {
        try {
            return this.tutorService.getTutorById(id);
        }catch (Exception e) {
            System.out.println(e);
            return  null;
        }
    }



    @PutMapping(path = "/update-tutor/{id}")
    public ResponseEntity<String> updateTutor(@PathVariable Long id, @RequestBody Tutor tutor) {
        try {
            this.tutorService.updateTutor(id, tutor);
            return new ResponseEntity<>("Tutor update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-all-tutor")
    public ResponseEntity<String> deleteAllTutor() {
        try {
            this.tutorService.deleteAllTutor();
            return new ResponseEntity<>("All tutor delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Somenthing went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-tutor/{id}")
    public ResponseEntity<String> deleteTutorById(@PathVariable Long id) {
        try {
            this.tutorService.deleteTutorById(id);
            return new ResponseEntity<>("Tutor with id: "+id+ " delete succesfully!", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Somenthing went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
