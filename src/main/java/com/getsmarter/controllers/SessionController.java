package com.getsmarter.controllers;

import com.getsmarter.entities.Center;
import com.getsmarter.entities.Formation;
import com.getsmarter.entities.Session;
import com.getsmarter.entities.Student;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.SessionService;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@ControllerAdvice
@AllArgsConstructor
@RequestMapping(path = "/session")
public class SessionController {

    private final SessionService sessionService;


    @PostMapping(path = "/save-session")
    public ResponseEntity<?> saveSession(@RequestBody Session session) {
        try {
            this.sessionService.saveSession(session);
            return new ResponseEntity<>(session, HttpStatus.CREATED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @GetMapping(path = "/get-all-session")
    public ResponseEntity<?> getAllSession() {

        try {
            List<Session> sessions = this.sessionService.getAllSession();
            return new ResponseEntity<>(sessions, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-all-student-session/{id}")
    public ResponseEntity<?> getAllStudentBySession(@PathVariable Long id) {
        try {
            Session session = this.sessionService.getSessionById(id);

            List<Student> students = session.getStudents();
            return new ResponseEntity<>(students, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse(e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }



    @GetMapping(path = "/get-session-frequently")
    public List<Session> getAllSessionFrequently() {
        return this.sessionService.getRecentlyAddedSessions();
    }


    @GetMapping(path = "/get-session/{id}")
    public ResponseEntity<?> getSessionById(@PathVariable Long id) {
        try {
            Session session = this.sessionService.getSessionById(id);
            return new ResponseEntity<>(session, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de recuperer cette session: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @PutMapping(path = "/update-session/{id}")
    public ResponseEntity<?> updateSession(@PathVariable Long id, @RequestBody Session session) {
        try {
            this.sessionService.updateSession(id, session);
            return new ResponseEntity<>(session, HttpStatus.ACCEPTED);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de modifier cette session: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @DeleteMapping(path = "/delete-all-session")
    public ResponseEntity<?> deleteAllSession() {
        try {
            this.sessionService.deleteAllSession();
            UserResponse userResponse = new UserResponse("Toutes les sessions ont ete supprime avec succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        }catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer toutes les sessions: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }


    @DeleteMapping(path = "/delete-session/{id}")
    public ResponseEntity<?> deleteSessionById(@PathVariable Long id) {
        try {
            this.sessionService.deleteSessionById(id);
            UserResponse userResponse = new UserResponse("Session supprimee avec succes !");
            return new ResponseEntity<>(userResponse, HttpStatus.OK);
        } catch (DataIntegrityViolationException ex) {
            UserResponse userResponse = new UserResponse("Erreur d'intégrité des données.\n"
                    +"Impossible de supprimer la session car elle est liée à d'autres donnees'");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(userResponse);
        }
        catch (Exception e) {
            System.out.println(e);
            UserResponse userResponse = new UserResponse("Impossible de supprimer cette session: "+e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
        }
    }
}
