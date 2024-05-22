package com.getsmarter.controllers;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Course;
import com.getsmarter.entities.Student;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.CourseService;
import lombok.AllArgsConstructor;
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
@RequestMapping(path = "/course", produces = MediaType.APPLICATION_JSON_VALUE)
public class CourseController {

    private final CourseService courseService;

    @PostMapping(path = "/save-course")
    public ResponseEntity<?> saveDureeCourse(@RequestBody Course course) {
        try {
            this.courseService.saveCourse(course);
            return new ResponseEntity<>(course, HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer la matiere: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @PostMapping(path = "/change-status/{id}")
    public ResponseEntity<?> changeCourseStatus(@PathVariable Long id) {
        try {
             this.courseService.changeStatut(id);
             UserResponse userResponse = new UserResponse("Matiere demarree avec succes !");
             return ResponseEntity.status(HttpStatus.OK).body(userResponse);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de demarrer la matiere: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @PostMapping(path = "/save-image/{id}")
    public ResponseEntity<?> uploadImageCenter(@PathVariable Long id, @RequestParam("image") MultipartFile file) {
        try {
            Course course = this.courseService.saveImageCenter(id, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(course);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'image: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
    }


    @GetMapping(path = "/get-all-course")
    public List<Course> getAllCourse() {
        return this.courseService.getAllCourse();
    }




    @GetMapping(path = "/get-course-frequently")
    public List<Course> getAllCourseFrequently() {
        return this.courseService.getRecentlyAddedCourses();
    }



    @GetMapping(path = "/get-course/{id}")
    public ResponseEntity<?> getDureeCourseById(@PathVariable Long id) {
        try {
            Course course = this.courseService.getCourseById(id);
            return ResponseEntity.status(HttpStatus.OK).body(course);
        }catch (Exception e){
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer cette matiere: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @PutMapping(path = "/update-course/{id}")
    public ResponseEntity<?> updateCourse(@PathVariable Long id, @RequestBody Course course) {
        try {
            this.courseService.updateCourse(id, course);
            return new ResponseEntity<>(course, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de modifier cette matiere: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-all-course")
    public ResponseEntity<?> deleteAllCourse() {
        try {
            this.courseService.deleteAllCourse();
            return null;
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer toutes les matieres: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @DeleteMapping(path = "/delete-course/{id}")
    public ResponseEntity<?> deleteCourseById(@PathVariable Long id) {
        try {
            this.courseService.deleteCourseById(id);
            return null;
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer la matiere: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
