package com.getsmarter.response;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<UserResponse> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        // Analyser l'exception et extraire les informations pertinentes
        String message = "Erreur d'intégrité des données : " + ex.getMessage();

        // Créer une réponse d'erreur personnalisée
        UserResponse errorResponse = new UserResponse(message);

        // Renvoyer la réponse d'erreur avec le code HTTP approprié
        return ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
    }
}
