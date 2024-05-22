package com.getsmarter.controllers;

import com.getsmarter.entities.Student;
import com.getsmarter.entities.Tutor;
import com.getsmarter.services.TutorService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@ControllerAdvice
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/tutor", produces = MediaType.APPLICATION_JSON_VALUE)
public class TutorController {

    private final TutorService tutorService;


    @PostMapping(path = "/save-tutor")
    public ResponseEntity<Tutor> saveTutor(@RequestBody Tutor tutor) {
            this.tutorService.saveTutor(tutor);
            return new ResponseEntity<>(tutor, HttpStatus.CREATED);
    }



    @GetMapping(path = "/get-all-tutor")
    public List<Tutor> getAllTutor() {
        return this.tutorService.getAllTutor();
    }



    @GetMapping(path = "/get-tutor-frequently")
    public List<Tutor> getAllTutorFrequently() {
        return this.tutorService.getRecentlyAddedTutors();
    }



    @GetMapping(path = "/get-tutor/{id}")
    public Tutor getTutorById(@PathVariable Long id) {
            return this.tutorService.getTutorById(id);
    }



    @PutMapping(path = "/update-tutor/{id}")
    public ResponseEntity<Tutor> updateTutor(@PathVariable Long id, @RequestBody Tutor tutor) {
            this.tutorService.updateTutor(id, tutor);
            return new ResponseEntity<>(tutor, HttpStatus.ACCEPTED);
    }



    @DeleteMapping(path = "/delete-all-tutor")
    public void deleteAllTutor() {
        try {
            this.tutorService.deleteAllTutor();
        }catch (Exception e) {
            throw new RuntimeException("Impossible de supprimer tous les tuteurs!");
        }
    }



    @DeleteMapping(path = "/delete-tutor/{id}")
    public void deleteTutorById(@PathVariable Long id) {
        try {
            this.tutorService.deleteTutorById(id);
        }catch (Exception e) {
            throw new RuntimeException("Impossible de supprimer le tuteur avec l'id: "+id);
        }
    }
}
