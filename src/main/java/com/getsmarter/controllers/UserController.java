package com.getsmarter.controllers;

import com.getsmarter.dto.AuthentificationDto;
import com.getsmarter.entities.User;
import com.getsmarter.response.UserResponse;
import com.getsmarter.services.UserService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@AllArgsConstructor
@ControllerAdvice
@CrossOrigin(origins = "http://localhost:4200",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE},
        maxAge = 3600)
@RequestMapping(path = "/user", produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

//    private final UserService userService;
//
//    private final AuthenticationManager authenticationManager;


    //Methode pour enregistrer un user
//    @PostMapping(path = "/save-user")
//    public ResponseEntity<?> saveAdmin(@RequestBody User user) {
//        try {
//            this.userService.saveAdmin(user);
//            return new ResponseEntity<>(user, HttpStatus.CREATED);
//        }
//        catch (Exception e) {
//            System.out.println(e);
//            UserResponse userResponse = new UserResponse("Impossible d'enregistrer l'utilisateur: "+e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
//        }
//    }



    //Methode pour activer le compte
//    @PostMapping(path = "/activation-user")
//    public ResponseEntity<?> activateCompteAdmin(@RequestBody Map<String, String> activation) {
//        try {
//            this.userService.activationCompte(activation);
//            return new ResponseEntity<>(activation, HttpStatus.CREATED);
//        }catch (Exception e) {
//            System.out.println(e);
//            UserResponse userResponse = new UserResponse("Impossible d'activer le compte de l'utlisateur: "+e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
//        }
//    }



    //Methode pour rafraichir le token
//    @PostMapping(path = "/refresh-token")
//    public @ResponseBody Map<String, String> refreshToken(@RequestBody Map<String, String> refreshTokenRequest) {
//            return this.jwtService.refreshToken(refreshTokenRequest);
//    }



    //Methode pour se connecter
//    @PostMapping(path = "/connexion-user")
//    public ResponseEntity<?> connexionCompteAdmin(@RequestBody AuthentificationDto authentificationDto) {
//        try {
//            final Authentication authenticate = authenticationManager.authenticate(
//                    new UsernamePasswordAuthenticationToken(authentificationDto.username(), authentificationDto.password())
//            );
//            //Si l'utilisateur est authentifier, on genere un jwt
//            if (authenticate.isAuthenticated()) {
//                return ResponseEntity.ok(this.jwtService.generate(authentificationDto.username()));
//            }else {
//                UserResponse userResponse = new UserResponse("email ou mot de passe incorrect !");
//                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userResponse);
//            }
//        }catch (Exception e) {
//            UserResponse userResponse = new UserResponse("Impossible de connecter l'utilisateur: email ou mot de passe incorrect");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userResponse);
//        }
//    }

//    @GetMapping("/profil")
//    public ResponseEntity<?> getProfil() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            // Vérifiez si l'utilisateur est authentifié
//            if (authentication.isAuthenticated()) {
//                // Récupérez les détails de l'utilisateur connecté
//                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//
//                // Vous pouvez accéder aux informations de l'utilisateur, par exemple :
//                String username = userDetails.getUsername();
//                // ... Autres informations sur l'utilisateur
//                User user = this.userService.getByEmail(username);
//                return new ResponseEntity<>(user, HttpStatus.OK);
//            }else {
//                UserResponse userResponse = new UserResponse("Aucun utilisateur trouve !");
//                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
//        }
//    }




    //Methode pour renitialiser le mot de passe
//    @PostMapping(path = "/reset-password")
//    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> activation) {
//        try {
//            this.userService.resetPassword(activation);
//            UserResponse userResponse = new UserResponse("Demande de renitialisation de mot de passe envoye avec succes !");
//            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
//        }
//        catch (Exception e) {
//            UserResponse userResponse = new UserResponse("Impossible de faire la demande de renitialisation de mot de passe: "+e.getMessage());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(userResponse);
//        }
//    }


    //Methode pour modifier le mot de passe
//    @PostMapping(path = "/update-password")
//    public ResponseEntity<?> updatePassword(@RequestBody Map<String, String> activation) {
//        try {
//            this.userService.updatePassword(activation);
//            return null;
//        }catch (Exception e) {
//            UserResponse userResponse = new UserResponse("Impossible de modifier le mot de passe de l'utilisateur: "+e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
//        }
//    }



    //Methode pour se deconnecter
//    @PostMapping(path = "/deconnexion-user")
//    public ResponseEntity<?> deconnecterCompteAdmin() {
//        try {
//            this.jwtService.deconnexion();
//            UserResponse userResponse = new UserResponse("Utilisateur deconnecte avec succes !");
//            return ResponseEntity.status(HttpStatus.OK).body(userResponse);
//        }catch (Exception e) {
//            System.out.println(e);
//            UserResponse userResponse = new UserResponse("Impossible de deconnecter l'utiisateur: "+e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
//        }
//    }


//    public User findUserByJwt(@RequestHeader("Authorization") String jwt) {
//
//    }



    //Methode pour recuperer un user grace a son id
//    @GetMapping(path = "/get-user/{id}")
//    public User getAdminById(@PathVariable Long id) {
//        try {
//            return this.userService.getAdminById(id);
//        }catch (Exception e) {
//            System.out.println(e);
//            return null;
//        }
//    }



    //Methode pour update un user
//    @PutMapping(path = "/update-user/{id}")
//    public ResponseEntity<?> updateAdmin(@PathVariable Long id, @RequestBody User user) {
//        try {
//            this.userService.updateAdmin(id, user);
//            return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
//        }catch (Exception e) {
//            System.out.println(e);
//            UserResponse userResponse = new UserResponse("Impossible de modifier l'utilisateur: "+e.getMessage());
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(userResponse);
//        }
//    }



    //Methode pour supprimer tous les admins
//    @DeleteMapping(path = "/delete-all")
//    public ResponseEntity<String> deleteAllAdmin() {
//        try {
//            this.userService.deleteAllAdmin();
//            return new ResponseEntity<>("All user was successfully delete !", HttpStatus.OK);
//        }catch (Exception e) {
//            System.out.println(e);
//            return new ResponseEntity<>("Something went wrong: " +e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }



    //Methode pour supprimer un user par son id
//    @DeleteMapping(path = "/delete-user/{id}")
//    public void deleteAdminById(@PathVariable Long id) {
//        try {
//            this.userService.deleteAdminById(id);
//        }catch (Exception e) {
//            System.out.println(e);
//        }
//    }

}
