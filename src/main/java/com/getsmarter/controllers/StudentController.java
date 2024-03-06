package com.getsmarter.controllers;

import com.getsmarter.entities.Student;
import com.getsmarter.services.StudentService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@AllArgsConstructor
@RequestMapping(path = "/student")
public class StudentController {

    private final StudentService studentService;


    @PostMapping(path = "/save-student")
    public ResponseEntity<String> saveStudent(@RequestBody Student student) {
        try {
            this.studentService.saveStudent(student);
            return new ResponseEntity<>("Student register successfully !", HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @GetMapping(path = "/get-all-student")
    public List<Student> getAllStudent() {
        return this.studentService.getAllStudent();
    }



    @GetMapping(path = "/get-student/{id}")
    public Student getStudentById(@PathVariable Long id) {
        try {
            return this.studentService.getStudentById(id);
        }catch (Exception e) {
            System.out.println(e);
            return null;
        }
    }



    @PutMapping(path = "/update-student/{id}")
    public ResponseEntity<String> updateStudent(@PathVariable Long id, @RequestBody Student student) {
        try {
            this.studentService.updateStudent(id, student);
            return new ResponseEntity<>("Student update successfully !", HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-all-student")
    public ResponseEntity<String> deleteAllStudent() {
        try {
            this.studentService.deleteAllStudent();
            return new ResponseEntity<>("All student delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }



    @DeleteMapping(path = "/delete-student/{id}")
    public ResponseEntity<String> deleteStudentById(@PathVariable Long id) {
        try {
            this.studentService.deleteStudentById(id);
            return new ResponseEntity<>("Student with id: "+id+ " delete successfully !", HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            return new ResponseEntity<>("Something went wrong: "+e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
