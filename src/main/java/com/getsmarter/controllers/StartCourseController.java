package com.getsmarter.controllers;

import com.getsmarter.dto.StartCourseDto;
import com.getsmarter.entities.StartCourse;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.StartCourseService;
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
@RequestMapping(path = "/startCourse", produces = MediaType.APPLICATION_JSON_VALUE)
public class StartCourseController {

    private final StartCourseService startCourseService;



    @GetMapping(path = "/get-all-start-course")
    public ResponseEntity<?> getAllStartCourses() {
        try {
            List<StartCourse> startCourses = this.startCourseService.getAllStartCourse();
            return new ResponseEntity<>(startCourses, HttpStatus.OK);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer la liste des matieres demarrees: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @GetMapping(path = "/get-start-course/{id}")
    public ResponseEntity<?> getStartCourse(@PathVariable Long id) {
        try {
            StartCourse startCourse = this.startCourseService.getStartCourse(id);
            return ResponseEntity.status(HttpStatus.OK).body(startCourse);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer cette matiere: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @DeleteMapping(path = "/delete-start-course/{id}")
    public ResponseEntity<?> deleteCourseById(@PathVariable Long id) {
        try {
            this.startCourseService.deleteStartCourseById(id);
            return null;
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer la matiere: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
